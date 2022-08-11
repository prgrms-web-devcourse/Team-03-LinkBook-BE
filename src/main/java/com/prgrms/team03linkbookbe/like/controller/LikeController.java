package com.prgrms.team03linkbookbe.like.controller;

import com.prgrms.team03linkbookbe.folder.dto.FolderListResponse;
import com.prgrms.team03linkbookbe.jwt.JwtAuthentication;
import com.prgrms.team03linkbookbe.like.dto.CreateLikeRequest;
import com.prgrms.team03linkbookbe.like.dto.CreateLikeResponse;
import com.prgrms.team03linkbookbe.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikeController {
    private final LikeService likeservice;

    @PostMapping("")
    public ResponseEntity<CreateLikeResponse> create(
            @Valid @RequestBody CreateLikeRequest requestDto,
            @AuthenticationPrincipal JwtAuthentication jwtAuthentication
    ) {

        CreateLikeResponse responseDto =
                likeservice.create(requestDto, jwtAuthentication.email);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FolderListResponse> getLikedFoldersByUserIds(
            Pageable pageable, @PathVariable Long id
    ) {

        FolderListResponse folders = likeservice.getLikedFoldersByUserId(id, pageable);

        return ResponseEntity.ok(folders);
    }

    @DeleteMapping("/{folderId}")
    public void delete(@PathVariable Long folderId, @AuthenticationPrincipal JwtAuthentication jwtAuthentication) {
        likeservice.delete(folderId, jwtAuthentication.email);
    }
}
