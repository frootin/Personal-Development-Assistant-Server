package ru.sfu.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DayNoteDto {
    private long id;
    private LocalDate day;
    private String text;
}
