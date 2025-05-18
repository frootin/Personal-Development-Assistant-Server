package ru.sfu.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultsDto {
    private List<TaskWindowDto> tasks;
    private List<PlanDto> plans;
    private List<DiaryEntryDto> diary;
    private List<DayNoteDto> notes;
}
