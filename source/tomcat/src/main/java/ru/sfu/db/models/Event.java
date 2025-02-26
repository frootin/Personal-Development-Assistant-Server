package ru.sfu.db.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user_events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;
    @NotNull
    @Column(name = "week_num", nullable = false)
    private int weekNum;
    @NotNull
    @Column(name = "day_of_week", nullable = false)
    private int dayOfWeek;
    @NotNull
    @Column(name = "event_name", nullable = false)
    private String eventName;
    @Column(nullable = false)
    private String place;
    @Column(name = "event_format")
    private String eventFormat;
    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    @NotNull
    @Column(name = "stop_time", nullable = false)
    private LocalTime stopTime;

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
        Event other = (Event) obj;
        if (id == null) {
            return false;
        } else return id.equals(other.id);
    }
}