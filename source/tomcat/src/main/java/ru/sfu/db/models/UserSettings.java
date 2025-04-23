package ru.sfu.db.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_settings")
public class UserSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonManagedReference
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    //@MapsId
    private User userId;
    @Column(name = "events_track_start_date")
    private LocalDate eventsTrackStartDate;
    @Column(name = "events_track_weeks_num")
    private int eventsTrackWeeksNum;
    @Column(name = "user_timezone")
    private String userTimezone;
    @Column(name = "week_start_day")
    private String weekStartDay;
    @Column(name = "show_event_schedule")
    private Boolean showEventSchedule;

    public UserSettings(User userId, String userTimezone) {
        this.userId = userId;
        this.eventsTrackStartDate = null;
        this.eventsTrackWeeksNum = 2;
        this.userTimezone = userTimezone;
        this.weekStartDay = "Monday";
        this.showEventSchedule = true;
    }

    public UserSettings(String userTimezone) {
        //this.userId = userId;
        this.eventsTrackStartDate = null;
        this.eventsTrackWeeksNum = 2;
        this.userTimezone = userTimezone;
        this.weekStartDay = "Monday";
        this.showEventSchedule = true;
    }

    @Override
    public int hashCode() {
        return 42;
    }
}
