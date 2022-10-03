package com.prgrms.team03linkbookbe.folder.dto;

import com.prgrms.team03linkbookbe.bookmark.dto.BookmarkResponse;
import com.prgrms.team03linkbookbe.comment.dto.CommentResponse;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.tag.dto.TagResponse;
import com.prgrms.team03linkbookbe.user.dto.UserSimpleResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FolderDetailResponse {

    private Long id;

    private String title;

    private String image;

    private String content;

    private OriginFolderResponse originFolder;

    private Boolean isPinned;

    private Boolean isPrivate;

    private UserSimpleResponseDto user;

    private List<BookmarkResponse> bookmarks;

    private List<CommentResponse> comments;

    private List<TagResponse> tags;

    private int likes;

    private Boolean isLiked;

    private LocalDateTime createdAt;


    public static FolderDetailResponse fromEntity(Folder folder, Boolean isLiked) {
        return FolderDetailResponse.builder()
            .id(folder.getId())
            .title(folder.getTitle())
            .likes(folder.getLikes())
            .image(folder.getImage())
            .content(folder.getContent())
            .isPinned(folder.getIsPinned())
            .isPrivate(folder.getIsPrivate())
            .isLiked(isLiked)
            .tags(folder.getFolderTags().stream()
                .map(folderTag -> TagResponse.fromEntity(folderTag.getTag()))
                .collect(Collectors.toList()))
            .user(UserSimpleResponseDto.fromEntity(folder.getUser()))
            .bookmarks(folder.getBookmarks().stream().map(BookmarkResponse::fromEntity).collect(
                Collectors.toList()))
            .comments(folder.getComments().stream().map(CommentResponse::fromEntity).collect(
                Collectors.toList()))
            .createdAt(folder.getCreatedAt())
            .build();
    }

    public static FolderDetailResponse fromEntity(Folder folder, Boolean isLiked,
        OriginFolderResponse originFolder) {
        return FolderDetailResponse.builder()
            .id(folder.getId())
            .title(folder.getTitle())
            .likes(folder.getLikes())
            .image(folder.getImage())
            .content(folder.getContent())
            .originFolder(originFolder)
            .isPinned(folder.getIsPinned())
            .isPrivate(folder.getIsPrivate())
            .isLiked(isLiked)
            .tags(folder.getFolderTags().stream()
                .map(folderTag -> TagResponse.fromEntity(folderTag.getTag()))
                .collect(Collectors.toList()))
            .user(UserSimpleResponseDto.fromEntity(folder.getUser()))
            .bookmarks(folder.getBookmarks().stream().map(BookmarkResponse::fromEntity).collect(
                Collectors.toList()))
            .comments(folder.getComments().stream().map(CommentResponse::fromEntity).collect(
                Collectors.toList()))
            .createdAt(folder.getCreatedAt())
            .build();
    }

}
