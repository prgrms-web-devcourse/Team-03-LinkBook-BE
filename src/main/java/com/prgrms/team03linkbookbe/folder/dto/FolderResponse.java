package com.prgrms.team03linkbookbe.folder.dto;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import com.prgrms.team03linkbookbe.user.dto.UserSimpleResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Builder
@Getter
public class FolderResponse {

    private Long id;

    private String title;

    private String image;

    private String content;

    private Boolean isPinned;

    private Boolean isPrivate;

    private UserSimpleResponseDto user;

    private List<TagCategory> tags;

    private int likes;

    private Boolean isLiked;

    private LocalDateTime createdAt;



    public static FolderResponse fromEntity(Folder folder, Boolean isLiked) {
        return FolderResponse.builder()
            .id(folder.getId())
            .title(folder.getTitle())
            .image(folder.getImage())
            .isPinned(folder.getIsPinned())
            .isPrivate(folder.getIsPrivate())
            .likes(folder.getLikes())
            .tags(folder.getFolderTags().stream().map(folderTag -> folderTag.getTag().getName()).collect(Collectors.toList()))
            .isLiked(isLiked)
            .user(UserSimpleResponseDto.fromEntity(folder.getUser()))
            .createdAt(folder.getCreatedAt())
            .build();
    }

}
