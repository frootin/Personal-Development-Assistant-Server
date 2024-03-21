package ru.sfu.db.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@NamedEntityGraph(name = "Plan.tasks",
        includeAllAttributes = true
)
@NamedEntityGraph(name = "notasks",
       attributeNodes = {@NamedAttributeNode("status"),
                         @NamedAttributeNode("name"),
                         @NamedAttributeNode("details")
                         }
)
@Table(name = "user_plans")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;
    @Column(name = "plan_name")
    private String name;
    @Column
    private String details;
    @Column(name = "status")
    private int status;
    @OneToMany(
            mappedBy = "plan",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<TaskPlan> tasks;
    /**@OneToMany(cascade = CascadeType.ALL, mappedBy = "plan", fetch = FetchType.EAGER)
    private List<Task> tasks;*/
}