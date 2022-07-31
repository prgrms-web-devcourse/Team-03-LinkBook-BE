package com.prgrms.team03linkbookbe.bookmark.controller;

import com.prgrms.team03linkbookbe.bookmark.dto.BookmarkRequest;
import com.prgrms.team03linkbookbe.bookmark.service.BookmarkService;
import com.prgrms.team03linkbookbe.user.entity.User;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookmarkController {

    BookmarkService bookmarkService;

    public BookmarkController(
        BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping("/api/bookmarks")
    @ResponseStatus(HttpStatus.OK)
    public void create(
        @Valid @RequestBody BookmarkRequest bookmarkRequest) {
        bookmarkService.create(bookmarkRequest);
    }

    @PutMapping("/api/bookmarks/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable Long id,
        @Valid @RequestBody BookmarkRequest bookmarkRequest,
        @AuthenticationPrincipal User user) {
        bookmarkService.update(user.getId(), id, bookmarkRequest);
    }

    @PutMapping("/api/bookmarks/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id,
        @AuthenticationPrincipal User user) {
        bookmarkService.delete(user.getId(), id);
    }


}
