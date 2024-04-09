package ru.sfu.db.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String email;
    @Column
    private String username;
    @Column(name="pass_hash")
    private String passhash;
    @Column(name = "display_name")
    private String displayName;
    @Column
    private String userpic;
    @Column
    private String interests;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL, mappedBy="userId")
    private UserSettings settings;

    public User(String email, String username, String passhash, String displayName, String userpic, String interests, LocalDateTime createdAt) {
        this.email = email;
        this.username = username;
        this.passhash = passhash;
        this.displayName = displayName;
        this.userpic = userpic;
        this.interests = interests;
        this.createdAt = createdAt;
    }

}
