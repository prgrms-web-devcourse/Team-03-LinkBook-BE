package com.prgrms.team03linkbookbe.like.controller;

import com.prgrms.team03linkbookbe.like.dto.CreateLikeRequestDto;
import com.prgrms.team03linkbookbe.like.dto.CreateLikeResponseDto;
import com.prgrms.team03linkbookbe.like.dto.UpdateLikeRequestDto;
import com.prgrms.team03linkbookbe.like.dto.UpdateLikeResponseDto;
import com.prgrms.team03linkbookbe.like.service.LikeService;
import com.prgrms.team03linkbookbe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {
    private final LikeService likeservice;

    @PostMapping("/")
    public ResponseEntity<CreateLikeResponseDto> create(
            @Valid @RequestBody CreateLikeRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {

        CreateLikeResponseDto responseDto =
                likeservice.create(requestDto, user.getId());

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        likeservice.delete(id, user.getId());
        return ResponseEntity.ok(id);
    }
}
