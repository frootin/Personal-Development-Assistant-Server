package ru.sfu.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sfu.db.models.DiaryEntry;
import ru.sfu.db.services.DiaryEntryService;
import ru.sfu.db.services.UserService;
import ru.sfu.objects.DiaryEntryDto;
import ru.sfu.util.JsonUtil;

import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/diary")
public class DiaryEntryController {
    private final DiaryEntryService diaryEntryService;
    private final UserService userService;

    @GetMapping
    public List<DiaryEntryDto> getEntries() {
        return JsonUtil.mapList(diaryEntryService.getAllEntries(userService.findById(1L)), DiaryEntryDto.class);
    }

    @GetMapping("{id}")
    public DiaryEntryDto getEntryById(@PathVariable long id) {
        return JsonUtil.ModelToDto(diaryEntryService.findById(id), DiaryEntryDto.class);
    }

    @PostMapping
    public DiaryEntryDto createEntry(@RequestBody JsonNode json) {
        DiaryEntry note = diaryEntryService.save(JsonUtil.JsonToSingleModel(json, DiaryEntryDto.class, DiaryEntry.class));
        return JsonUtil.ModelToDto(note, DiaryEntryDto.class);
    }

    @PutMapping
    public DiaryEntryDto updateEntry(@RequestBody JsonNode json) {
        DiaryEntry note = diaryEntryService.updateOrInsert(JsonUtil.JsonToSingleModel(json, DiaryEntryDto.class, DiaryEntry.class));
        return JsonUtil.ModelToDto(note, DiaryEntryDto.class);
    }
}
