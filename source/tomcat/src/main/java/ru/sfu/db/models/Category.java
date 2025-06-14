package ru.sfu.db.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(nullable = false)
    private String title;
    @NotNull
    @Column(nullable = false)
    private String color;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;
    @NotNull
    @Column(name = "on_watch", nullable = false)
    private Boolean onWatch;
    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    public Category(String title, String color, User userId) {
        this.title = title;
        this.color = color;
        this.userId = userId;
        this.onWatch = true;
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
        Category other = (Category) obj;
        if (id == null) {
            return false;
        } else return id.equals(other.id);
    }

    public static List<Category> getInitialCategories(User userId) {
        List<Category> categories = new ArrayList<Category>();
        categories.add(new Category("Здоровье", "#03fc4e", userId));
        categories.add(new Category("Семья", "#a103fc", userId));
        categories.add(new Category("Учёба", "#fc8c03", userId));
        categories.add(new Category("Спорт", "#0366fc", userId));
        return categories;
    }
}
