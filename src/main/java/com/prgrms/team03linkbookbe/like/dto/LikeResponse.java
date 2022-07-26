package com.prgrms.team03linkbookbe.like.dto;

import com.prgrms.team03linkbookbe.comment.entity.Comment;
import com.prgrms.team03linkbookbe.folder.dto.FolderResponse;
import com.prgrms.team03linkbookbe.user.dto.UserSimpleResponseDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeResponse {

    private Long id;

    private FolderResponse folder;

    private UserSimpleResponseDto user;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static LikeResponse fromEntity(Comment comment) {
        return LikeResponse.builder()
                .id(comment.getId())
                .folder(FolderResponse.fromEntity(comment.getFolder(), true))
                .user(UserSimpleResponseDto.fromEntity(comment.getUser()))
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
