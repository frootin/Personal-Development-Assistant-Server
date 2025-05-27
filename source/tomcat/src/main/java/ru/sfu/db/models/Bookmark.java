package ru.sfu.db.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "bookmarks")
public class Bookmark {
    public static final String TAG_SPLIT = " ";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;

    @NotBlank(message = "The name is missing")
    @Column(name = "bookmark_name", nullable = false)
    private String name;

    //@NotBlank(message = "The entry text is missing")
    @Column(name = "bookmark_link")
    private String link;

    //@NotBlank(message = "The entry text is missing")
    @Column(name = "details", nullable = false)
    private String details;

    @Column(name = "tags")
    private String[] tags;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void setTags(String tagsString, String tagSplit) {
        this.tags = tagsString.split(tagSplit);
    }

    public void setTags(String tagsString) {
        setTags(tagsString, TAG_SPLIT);
    }

    @Override
    public int hashCode() {
        return 42;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Bookmark other = (Bookmark) obj;
        return Objects.equals(id, other.getId());
    }
}
