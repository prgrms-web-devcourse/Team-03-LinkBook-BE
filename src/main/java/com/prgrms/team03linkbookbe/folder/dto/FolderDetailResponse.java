package com.prgrms.team03linkbookbe.folder.dto;

import com.prgrms.team03linkbookbe.bookmark.dto.BookmarkResponse;
import com.prgrms.team03linkbookbe.comment.dto.CommentResponse;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import com.prgrms.team03linkbookbe.user.dto.UserSimpleResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FolderDetailResponse {

    private Long id;

    private String title;

    private String image;

    private String content;

    private Long originId;

    private Boolean isPinned;

    private Boolean isPrivate;

    private UserSimpleResponseDto user;

    private List<BookmarkResponse> bookmarks;

    private List<TagCategory> tags;

    private int likes;

    private List<CommentResponse> comments;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public static FolderDetailResponse fromEntity(Folder folder, int likes) {
        return FolderDetailResponse.builder()
            .id(folder.getId())
            .title(folder.getTitle())
            .image(folder.getImage())
            .content(folder.getContent())
            .originId(folder.getOriginId())
            .isPinned(folder.getIsPinned())
            .isPrivate(folder.getIsPrivate())
            .user(UserSimpleResponseDto.fromEntity(folder.getUser()))
            .bookmarks(folder.getBookmarks().stream().map(BookmarkResponse::fromEntity).collect(
                Collectors.toList()))
            .comments(folder.getComments().stream().map(CommentResponse::fromEntity).collect(
                Collectors.toList()))
            .createdAt(folder.getCreatedAt())
            .updatedAt(folder.getUpdatedAt())
            .build();
    }

}
