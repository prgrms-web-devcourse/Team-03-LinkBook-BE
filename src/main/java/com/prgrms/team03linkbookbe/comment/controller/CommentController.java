package com.prgrms.team03linkbookbe.comment.controller;

import com.prgrms.team03linkbookbe.comment.dto.CreateCommentRequestDto;
import com.prgrms.team03linkbookbe.comment.dto.CreateCommentResponseDto;
import com.prgrms.team03linkbookbe.comment.dto.UpdateCommentRequestDto;
import com.prgrms.team03linkbookbe.comment.dto.UpdateCommentResponseDto;
import com.prgrms.team03linkbookbe.comment.service.CommentService;
import com.prgrms.team03linkbookbe.jwt.JwtAuthentication;
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
            @AuthenticationPrincipal JwtAuthentication jwtAuthentication
    ) {

        CreateCommentResponseDto responseDto =
                commentService.create(requestDto, jwtAuthentication.email);

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/")
    public ResponseEntity<UpdateCommentResponseDto> update(
            @Valid @RequestBody UpdateCommentRequestDto requestDto,
            @AuthenticationPrincipal JwtAuthentication jwtAuthentication
    ) {

        UpdateCommentResponseDto responseDto =
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
