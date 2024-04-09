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
    private Long id;
    @JsonManagedReference
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @MapsId
    private User userId;
    @Column(name = "events_track_start_date")
    private LocalDate eventsTrackStartDate;
    @Column(name = "events_track_weeks_num")
    private int eventsTrackWeeksNum;
}
