package com.prgrms.team03linkbookbe.comment.controller;

import com.prgrms.team03linkbookbe.comment.dto.*;
import com.prgrms.team03linkbookbe.comment.service.CommentService;
import com.prgrms.team03linkbookbe.jwt.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/")
    public ResponseEntity<CreateCommentResponse> create(
            @Valid @RequestBody CreateCommentRequest requestDto,
            @AuthenticationPrincipal JwtAuthentication jwtAuthentication
    ) {

        CreateCommentResponse responseDto =
                commentService.create(requestDto, jwtAuthentication.email);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/folders/{id}")
    public ResponseEntity<CommentListResponse> getAllByFolderId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(commentService.getAllByFolder(id));
    }

    @PutMapping("/")
    public ResponseEntity<UpdateCommentResponse> update(
            @Valid @RequestBody UpdateCommentRequest requestDto,
            @AuthenticationPrincipal JwtAuthentication jwtAuthentication
    ) {

        UpdateCommentResponse responseDto =
                commentService.update(requestDto, jwtAuthentication.email);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtAuthentication jwtAuthentication) {
        commentService.delete(id, jwtAuthentication.email);
        return ResponseEntity.ok(id);
    }
}
