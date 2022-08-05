package com.prgrms.team03linkbookbe.tag.controller;

import com.prgrms.team03linkbookbe.tag.dto.TagResponse;
import com.prgrms.team03linkbookbe.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/api/tags")
    public ResponseEntity<TagResponse> manageTag(){
        return ResponseEntity.ok(tagService.manage());
    }


}
