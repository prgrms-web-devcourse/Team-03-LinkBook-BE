package com.prgrms.team03linkbookbe.folder.controller;

import com.prgrms.team03linkbookbe.folder.dto.CreateFolderRequest;
import com.prgrms.team03linkbookbe.folder.dto.FolderDetailResponse;
import com.prgrms.team03linkbookbe.folder.dto.FolderIdResponse;
import com.prgrms.team03linkbookbe.folder.dto.FolderListByUserResponse;
import com.prgrms.team03linkbookbe.folder.dto.FolderListResponse;
import com.prgrms.team03linkbookbe.folder.dto.PinnedListResponse;
import com.prgrms.team03linkbookbe.folder.dto.RootTagRequest;
import com.prgrms.team03linkbookbe.folder.dto.TagRequest;
import com.prgrms.team03linkbookbe.folder.service.FolderService;
import com.prgrms.team03linkbookbe.jwt.JwtAuthentication;
import com.prgrms.team03linkbookbe.jwt.exception.IllegalTokenException;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FolderController {

    FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping("/api/folders")
    public ResponseEntity<FolderListResponse> readAll(Pageable pageable,
        @AuthenticationPrincipal JwtAuthentication auth,
        @RequestParam @Nullable String title) {
        FolderListResponse all;

        if (title == null) {
            all = folderService.getAll(pageable, auth);
        } else {
            all = folderService.getAllByTitle(pageable, title, auth);
        }

        return ResponseEntity.ok(all);
    }

    @GetMapping("/api/folders/users/{userId}")
    public ResponseEntity<FolderListByUserResponse> readAllByUser(@PathVariable Long userId,
        @AuthenticationPrincipal JwtAuthentication auth,
        @RequestParam @Nullable String isPrivate, Pageable pageable) {
        FolderListByUserResponse allByUser;
        if(auth != null){
            allByUser = folderService.getAllByUser(userId, auth.email,
                isPrivate, pageable);
        }
        else {
            allByUser = folderService.getAllByUser(userId, null,
                isPrivate, pageable);
        }
        return ResponseEntity.ok(allByUser);
    }

    @GetMapping("/api/folders/pinned")
    public ResponseEntity<PinnedListResponse> readAllPinned(
        @AuthenticationPrincipal JwtAuthentication auth) {
        if(auth == null) {
            throw new IllegalTokenException();
        }
        PinnedListResponse response = folderService.getAllPinned(auth.email);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/api/folders/{id}")
    public ResponseEntity<FolderDetailResponse> readDetail(@PathVariable Long id,
        @AuthenticationPrincipal JwtAuthentication auth) {
        FolderDetailResponse detail = folderService.detail(id, auth);
        return ResponseEntity.ok(detail);
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

    @GetMapping("/api/folders/root-tag")
    public ResponseEntity<FolderListResponse> readByRootTag(
        @RequestBody RootTagRequest rootTagRequest,
        Pageable pageable, @AuthenticationPrincipal JwtAuthentication auth) {
        FolderListResponse byRootTag = folderService.getByRootTag(rootTagRequest, pageable, auth);
        return ResponseEntity.ok().body(byRootTag);
    }

    @GetMapping("/api/folders/tag")
    public ResponseEntity<FolderListResponse> readByTag(@RequestBody TagRequest tagRequest,
        Pageable pageable, @AuthenticationPrincipal JwtAuthentication auth) {
        FolderListResponse byTag = folderService.getByTag(tagRequest, pageable, auth);
        return ResponseEntity.ok().body(byTag);
    }
}
