package ru.sfu.db.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

@ToString
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @NotBlank
    @Column(nullable = false)
    private String username;
    @NotBlank
    @Column(name="pass_hash", nullable = false)
    private String password;
    @Column(name = "display_name")
    private String displayName;
    @Column
    private String userpic;
    @Column
    private String interests;
    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL, mappedBy="userId", orphanRemoval = true)
    private UserSettings settings;
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    public User(String email, String username, String password, String displayName, String userpic, String interests) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.userpic = userpic;
        this.interests = interests;
        this.role = Role.ROLE_USER;
        //this.createdAt = createdAt;
    }

    public User(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return 42;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
