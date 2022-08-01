package com.prgrms.team03linkbookbe.folder.dto;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.tag.entity.Tag;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import com.prgrms.team03linkbookbe.user.dto.UserResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FolderResponse {

    private Long id;

    private String title;

    private String image;

    private String content;

    private Boolean isPinned;

    private Boolean isPrivate;

    private UserResponse user;

    private List<TagCategory> tags;

    private Long likes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    public static FolderResponse fromEntity(Folder folder) {
        return FolderResponse.builder()
            .id(folder.getId())
            .title(folder.getTitle())
            .content(folder.getContent())
            .image(folder.getImage())
            .isPinned(folder.getIsPinned())
            .isPrivate(folder.getIsPrivate())
            .tags(folder.getFolderTags().stream().map(folderTag -> folderTag.getTag().getName()).collect(Collectors.toList()))
            .user(UserResponse.fromEntity(folder.getUser()))
            .createdAt(folder.getCreatedAt())
            .updatedAt(folder.getUpdatedAt())
            .build();
    }

}
