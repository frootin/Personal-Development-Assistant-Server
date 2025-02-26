package ru.sfu.db.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable = false)
    private String email;
    @NotBlank
    @Column(nullable = false)
    private String username;
    @NotBlank
    @Column(name="pass_hash", nullable = false)
    private String passhash;
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
    @OneToOne(cascade = CascadeType.ALL, mappedBy="userId")
    private UserSettings settings;

    public User(String email, String username, String passhash, String displayName, String userpic, String interests) {
        this.email = email;
        this.username = username;
        this.passhash = passhash;
        this.displayName = displayName;
        this.userpic = userpic;
        this.interests = interests;
        //this.createdAt = createdAt;
    }

    @Override
    public int hashCode() {
        return 42;
    }
}
