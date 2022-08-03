package com.prgrms.team03linkbookbe.like.dto;

import com.prgrms.team03linkbookbe.comment.entity.Comment;
import com.prgrms.team03linkbookbe.folder.dto.FolderResponse;
import com.prgrms.team03linkbookbe.user.dto.UserResponse;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeResponseDto {
    private Long id;

    private FolderResponse folder;

    private UserResponse user;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static LikeResponseDto fromEntity(Comment comment) {
        return LikeResponseDto.builder()
                .id(comment.getId())
                .folder(FolderResponse.fromEntity(comment.getFolder()))
                .user(UserResponse.fromEntity(comment.getUser()))
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
