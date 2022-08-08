package com.prgrms.team03linkbookbe.rootTag.controller;

import com.prgrms.team03linkbookbe.rootTag.dto.RootTagListResponse;
import com.prgrms.team03linkbookbe.rootTag.dto.RootTagResponse;
import com.prgrms.team03linkbookbe.rootTag.entity.RootTagCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootTagController {

    @GetMapping("/api/tags")
    public ResponseEntity<RootTagListResponse> findAll(){
        return ResponseEntity.ok(RootTagListResponse.fromEntity(RootTagCategory.values()));
    }


}
