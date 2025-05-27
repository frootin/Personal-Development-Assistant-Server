package ru.sfu.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sfu.db.models.Bookmark;
import ru.sfu.db.services.BookmarkService;
import ru.sfu.db.services.UserService;
import ru.sfu.exceptions.EmptyBookmarkException;
import ru.sfu.objects.BookmarkDto;
import ru.sfu.util.JsonUtil;

import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;
    private final UserService userService;

    @GetMapping
    public List<BookmarkDto> getEntries() {
        return JsonUtil.mapList(bookmarkService.getAllEntries(userService.findById(1L)), BookmarkDto.class);
    }

    @GetMapping("{id}")
    public BookmarkDto getEntryById(@PathVariable long id) {
        return JsonUtil.ModelToDto(bookmarkService.findById(id), BookmarkDto.class);
    }

    @PostMapping
    public BookmarkDto createEntry(@RequestBody JsonNode json) throws EmptyBookmarkException {
        Bookmark bookmark = bookmarkService.save(JsonUtil.JsonToSingleModel(json, BookmarkDto.class, Bookmark.class));
        if (bookmark == null) return null;
        return JsonUtil.ModelToDto(bookmark, BookmarkDto.class);
    }

    @PutMapping
    public BookmarkDto updateEntry(@RequestBody JsonNode json) {
        Bookmark bookmark = bookmarkService.updateOrInsert(JsonUtil.JsonToSingleModel(json, BookmarkDto.class, Bookmark.class));
        if (bookmark == null) return null;
        return JsonUtil.ModelToDto(bookmark, BookmarkDto.class);
    }
}
