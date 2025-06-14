package ru.sfu.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sfu.db.models.DayNote;
import ru.sfu.db.services.DayNoteService;
import ru.sfu.db.services.UserService;
import ru.sfu.objects.DayNoteDto;
import ru.sfu.objects.TaskWindowDto;
import ru.sfu.util.JsonUtil;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/notes")
public class DayNoteController {
    private final DayNoteService dayNoteService;
    private final UserService userService;

    @PostMapping
    public DayNoteDto createNote(@RequestBody JsonNode json) {
        DayNote note = dayNoteService.save(JsonUtil.JsonToSingleModel(json, DayNoteDto.class, DayNote.class));
        note.setUserId(userService.getCurrentUser());
        return JsonUtil.ModelToDto(note, DayNoteDto.class);
    }

    @PutMapping
    public DayNoteDto updateNote(@RequestBody JsonNode json) {
        DayNote note = dayNoteService.save(JsonUtil.JsonToSingleModel(json, DayNoteDto.class, DayNote.class));
        note.setUserId(userService.getCurrentUser());
        return JsonUtil.ModelToDto(note, DayNoteDto.class);
    }
}
