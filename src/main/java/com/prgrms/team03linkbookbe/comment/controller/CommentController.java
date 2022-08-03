package com.prgrms.team03linkbookbe.comment.controller;

import com.prgrms.team03linkbookbe.comment.dto.CreateCommentRequestDto;
import com.prgrms.team03linkbookbe.comment.dto.CreateCommentResponseDto;
import com.prgrms.team03linkbookbe.comment.dto.UpdateCommentRequestDto;
import com.prgrms.team03linkbookbe.comment.dto.UpdateCommentResponseDto;
import com.prgrms.team03linkbookbe.comment.service.CommentService;
import com.prgrms.team03linkbookbe.user.entity.User;
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
    public ResponseEntity<CreateCommentResponseDto> create(
            @Valid @RequestBody CreateCommentRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {

        CreateCommentResponseDto responseDto =
                commentService.create(requestDto, user.getId());

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/")
    public ResponseEntity<UpdateCommentResponseDto> update(
            @Valid @RequestBody UpdateCommentRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {

        UpdateCommentResponseDto responseDto =
                commentService.update(requestDto, user.getId());

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        commentService.delete(id, user.getId());
        return ResponseEntity.ok(id);
    }
}
