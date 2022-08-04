package com.prgrms.team03linkbookbe.folder.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.prgrms.team03linkbookbe.bookmark.dto.BookmarkResponse;
import com.prgrms.team03linkbookbe.comment.dto.CommentResponseDto;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.tag.entity.Tag;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import com.prgrms.team03linkbookbe.user.dto.UserResponse;
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

    private Long originId;

    private Boolean isPinned;

    private Boolean isPrivate;

    private UserResponse user;

    private List<BookmarkResponse> bookmarks;

    private List<TagCategory> tags;

    private int likes;

    private LocalDateTime createdAt;



    public static FolderDetailResponse fromEntity(Folder folder) {
        return FolderDetailResponse.builder()
            .id(folder.getId())
            .title(folder.getTitle())
            .likes(folder.getLikes())
            .image(folder.getImage())
            .content(folder.getContent())
            .originId(folder.getOriginId())
            .isPinned(folder.getIsPinned())
            .isPrivate(folder.getIsPrivate())
            .user(UserResponse.fromEntity(folder.getUser()))
            .tags(folder.getFolderTags().stream().map(f -> f.getTag().getName()).collect(Collectors.toList()))
            .bookmarks(folder.getBookmarks().stream().map(BookmarkResponse::fromEntity).collect(
                Collectors.toList()))
            .createdAt(folder.getCreatedAt())
            .build();
    }

}
