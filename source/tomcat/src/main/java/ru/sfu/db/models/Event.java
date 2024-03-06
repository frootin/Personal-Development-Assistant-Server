package ru.sfu.db.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

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
    @Column(name = "week_num")
    private int weekNum;
    @Column(name = "day_of_week")
    private int dayOfWeek;
    @Column(name = "event_name")
    private String eventName;
    private String place;
    @Column(name = "event_format")
    private String eventFormat;
    @Column(name = "start_time")
    private LocalTime startTime;
    @Column(name = "stop_time")
    private LocalTime stopTime;
}