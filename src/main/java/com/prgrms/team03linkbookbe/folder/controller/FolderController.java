package com.prgrms.team03linkbookbe.folder.controller;

import com.prgrms.team03linkbookbe.folder.dto.*;
import com.prgrms.team03linkbookbe.folder.service.FolderService;
import com.prgrms.team03linkbookbe.jwt.JwtAuthentication;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static java.lang.Boolean.parseBoolean;

@RestController
public class FolderController {

    FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping("/api/folders")
    public ResponseEntity<FolderListResponse> readAll(Pageable pageable) {
        FolderListResponse all = folderService.getAll(pageable);
        return ResponseEntity.ok(all);
    }

    @GetMapping("/api/folders/users/{userId}")
    public ResponseEntity<FolderListByUserResponse> readAllByUser(@PathVariable Long userId, @RequestParam String isPrivate, Pageable pageable) {
        FolderListByUserResponse allByUser = folderService.getAllByUser(userId, parseBoolean(isPrivate), pageable);
        return ResponseEntity.ok(allByUser);
    }

    @GetMapping("/api/folders/{id}")
    public ResponseEntity<FolderDetailResponse> readDetail(@PathVariable Long id) {
        FolderDetailResponse detail = folderService.detail(id);
        return ResponseEntity.ok(detail);
    }

    @GetMapping("/api/folders/{title}")
    public ResponseEntity<FolderListResponse> readAllByTitle(Pageable pageable, @PathVariable String title) {
        FolderListResponse all = folderService.getAllByTitle(pageable, title);
        return ResponseEntity.ok(all);
    }

    @PostMapping("/api/folders")
    public ResponseEntity<FolderIdResponse> create(
            @Valid @RequestBody CreateFolderRequest createFolderRequest,
            @AuthenticationPrincipal JwtAuthentication auth) {
        FolderIdResponse create = folderService.create(auth, createFolderRequest);
        return ResponseEntity.ok(create);
    }

    @PutMapping("/api/folders/{id}")
    public ResponseEntity<FolderIdResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody CreateFolderRequest createFolderRequest,
                                                   @AuthenticationPrincipal JwtAuthentication auth) {
        FolderIdResponse update = folderService.update(auth.email, id, createFolderRequest);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/api/folders/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal JwtAuthentication auth) {
        folderService.delete(auth.email, id);
    }


}
