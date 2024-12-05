package ru.sfu.db.models;

import com.vladmihalcea.hibernate.type.array.IntArrayType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TypeDef;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "repeats")
@TypeDef(
        typeClass = IntArrayType.class,
        defaultForType = int[].class
)
public class Repeat {
    public static final String WEEKLY = "week";
    public static final String DAILY = "day";
    public static final String MONTHLY = "month";
    public static final String YEARLY = "year";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;
    @Column(name = "task_name")
    private String name;
    @Column(name="details")
    private String details;
    @Column(name="estimate")
    private int estimate;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category categoryId;
    @Column(name = "date_start")
    private LocalDate startDate;
    @Column(name = "date_end")
    private LocalDate stopDate;
    @Column(name = "time_start")
    private LocalTime startTime;
    @Column(name = "time_end")
    private LocalTime stopTime;
    @Column(name = "task_timezone")
    private String timezone;
    @Column(name = "repeat_start")
    private LocalDate repeatStart;
    @Column(name = "repeat_end")
    private LocalDate repeatEnd;
    @Column(name = "repeat_term")
    private String repeatTerm;
    //@Type(ListArrayType.class)
    @Column(
            name = "repeat_days",
            columnDefinition = "integer[]"
    )
    private int[] repeatDays;
    @Column(name = "number_of_repeats")
    private Integer numberOfRepeats;
    @Column(name = "repeat_interval")
    private Integer repeatInterval;
    @ManyToOne
    @JoinColumn(name = "plan_id", referencedColumnName = "id")
    private Plan planId;

    public Repeat(Task task, String repeatTerm, int[] repeatDays, LocalDate repeatStart, LocalDate repeatEnd,
                  int numberOfRepeats, Integer repeatInterval) {
        this.name = task.getName();
        this.details = task.getDetails();
        this.categoryId = task.getCategoryId();
        this.estimate = task.getEstimate();
        this.userId= task.getUserId();
        this.startDate = task.getStartDate();
        this.stopDate = task.getStopDate();
        this.startTime = task.getStartTime();
        this.stopTime = task.getStopTime();
        this.timezone = task.getTimezone();
        if (task.getPlan() != null) {
            this.planId = task.getPlan().getPlan();
        }
        this.repeatTerm = repeatTerm;
        this.repeatDays = repeatDays;
        this.repeatStart = repeatStart;
        this.repeatEnd = repeatEnd;
        this.numberOfRepeats = numberOfRepeats;
        this.repeatInterval = repeatInterval;
    }
}
