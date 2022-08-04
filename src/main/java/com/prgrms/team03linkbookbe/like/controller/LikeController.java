package com.prgrms.team03linkbookbe.like.controller;

import com.prgrms.team03linkbookbe.folder.dto.FolderIdResponse;
import com.prgrms.team03linkbookbe.folder.dto.FolderResponse;
import com.prgrms.team03linkbookbe.jwt.JwtAuthentication;
import com.prgrms.team03linkbookbe.like.dto.CreateLikeRequest;
import com.prgrms.team03linkbookbe.like.dto.CreateLikeResponse;
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
    public ResponseEntity<CreateLikeResponse> create(
            @Valid @RequestBody CreateLikeRequest requestDto,
            @AuthenticationPrincipal JwtAuthentication jwtAuthentication
    ) {

        CreateLikeResponse responseDto =
                likeservice.create(requestDto, jwtAuthentication.email);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<FolderResponse>> getLikedFoldersByUserIds(
            @PathVariable Long id
    ) {

        List<FolderResponse> folders = likeservice.getLikedFoldersByUserId(id);

        return ResponseEntity.ok(folders);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id, @AuthenticationPrincipal JwtAuthentication jwtAuthentication) {
        likeservice.delete(id, jwtAuthentication.email);
        return ResponseEntity.ok(id);
    }
}
