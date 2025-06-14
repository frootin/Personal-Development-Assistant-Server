package ru.sfu.controllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.web.bind.annotation.*;
import ru.sfu.db.models.*;
import ru.sfu.db.services.*;
import ru.sfu.objects.*;
import ru.sfu.util.JsonUtil;

import java.util.List;


@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/storage")
public class StorageController {
    private final PlanService planService;
    private final UserService userService;
    private final TaskService taskService;
    private final DayNoteService dayNoteService;
    private final DiaryEntryService diaryEntryService;
    private final BookmarkService bookmarkService;


    @PostMapping
    public SearchResultsDto search(@RequestBody StorageSearchDto searchDto) {
        User user = userService.getCurrentUser();
        SearchResultsDto searchResultsDto = new SearchResultsDto();
        for (String entityType: searchDto.getEntityTypes()) {
            if (entityType.equals(EntityTypes.TASK)) {
                List<Task> tasks = taskService.filterForStorage(user,
                        searchDto);
                searchResultsDto.setTasks(JsonUtil.mapList(tasks, TaskWindowDto.class));
            }
            if (entityType.equals(EntityTypes.PLAN)) {
                List<Plan> plans = planService.filterForStorage(user,
                        searchDto);
                ModelMapper modelMapper = new ModelMapper();
                TypeMap<Plan, PlanDto> propertyMapper = modelMapper.createTypeMap(Plan.class, PlanDto.class);
                propertyMapper.addMappings(mapper -> mapper.skip(PlanDto::setTasks));
                List<PlanDto> dtoPlans = JsonUtil.mapListWithSkips(plans, PlanDto.class, modelMapper);
                searchResultsDto.setPlans(dtoPlans);
            }
            if (entityType.equals(EntityTypes.DAY_NOTE)) {
                List<DayNote> notes = dayNoteService.filterForStorage(user,
                        searchDto);
                searchResultsDto.setNotes(JsonUtil.mapList(notes, DayNoteDto.class));
            }
            if (entityType.equals(EntityTypes.DIARY_ENTRY)) {
                List<DiaryEntry> notes = diaryEntryService.filterForStorage(user,
                        searchDto);
                searchResultsDto.setDiary(JsonUtil.mapList(notes, DiaryEntryDto.class));
            }
            if (entityType.equals(EntityTypes.BOOKMARK)) {
                List<Bookmark> notes = bookmarkService.filterForStorage(user,
                        searchDto);
                searchResultsDto.setDiary(JsonUtil.mapList(notes, DiaryEntryDto.class));
            }
        }
        return searchResultsDto;
    }
}
