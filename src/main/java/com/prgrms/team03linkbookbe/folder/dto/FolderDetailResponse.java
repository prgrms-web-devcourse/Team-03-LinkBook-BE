package com.prgrms.team03linkbookbe.folder.dto;

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
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FolderDetailResponse {
    private Long id;

    private String name;

    private String image;

    private String content;

    private Long originId;

    private Boolean isPinned;

    private Boolean isPrivate;

    private UserResponse user;

    private List<BookmarkResponse> bookmarks;

    private List<TagCategory> tags;

    private Long likes;

    private List<CommentResponseDto> comments;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    @Builder
    public FolderDetailResponse(Long id, String name, String image, String content,
        Long originId, Boolean isPinned, Boolean isPrivate,
        UserResponse user,
        List<BookmarkResponse> bookmarks,
        List<TagCategory> tags, Long likes,
        List<CommentResponseDto> comments, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.content = content;
        this.originId = originId;
        this.isPinned = isPinned;
        this.isPrivate = isPrivate;
        this.user = user;
        this.bookmarks = bookmarks;
        this.tags = tags;
        this.likes = likes;
        this.comments = comments;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }



    public static FolderDetailResponse fromEntity(Folder folder){
        return FolderDetailResponse.builder()
            .id(folder.getId())
            .name(folder.getName())
            .image(folder.getImage())
            .content(folder.getContent())
            .originId(folder.getOriginId())
            .isPinned(folder.getIsPinned())
            .isPrivate(folder.getIsPrivate())
            .tags(folder.getTags().stream().map(Tag::getName).collect(Collectors.toList()))
            .user(UserResponse.fromEntity(folder.getUser()))
            .bookmarks(folder.getBookmarks().stream().map(BookmarkResponse::fromEntity).collect(
                Collectors.toList()))
            .comments(folder.getComments().stream().map(CommentResponseDto::fromEntity).collect(
                Collectors.toList()))
            .createdAt(folder.getCreatedAt())
            .updatedAt(folder.getUpdatedAt())
            .build();
    }

}