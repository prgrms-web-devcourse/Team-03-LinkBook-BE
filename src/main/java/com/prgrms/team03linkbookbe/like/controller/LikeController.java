package com.prgrms.team03linkbookbe.like.controller;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.jwt.JwtAuthentication;
import com.prgrms.team03linkbookbe.like.dto.CreateLikeRequestDto;
import com.prgrms.team03linkbookbe.like.dto.CreateLikeResponseDto;
import com.prgrms.team03linkbookbe.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {
    private final LikeService likeservice;

    @PostMapping("/")
    public ResponseEntity<CreateLikeResponseDto> create(
            @Valid @RequestBody CreateLikeRequestDto requestDto,
            @AuthenticationPrincipal JwtAuthentication jwtAuthentication
    ) {

        CreateLikeResponseDto responseDto =
                likeservice.create(requestDto, jwtAuthentication.email);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Folder>> getLikedFoldersByUserIds(
            @PathVariable Long id
    ) {

        List<Folder> folders = likeservice.getLikedFoldersByUserId(id);

        return ResponseEntity.ok(folders);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id, @AuthenticationPrincipal JwtAuthentication jwtAuthentication) {
        likeservice.delete(id, jwtAuthentication.email);
        return ResponseEntity.ok(id);
    }
}
