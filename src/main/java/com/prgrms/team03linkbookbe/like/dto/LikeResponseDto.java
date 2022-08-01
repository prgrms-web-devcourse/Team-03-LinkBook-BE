package com.prgrms.team03linkbookbe.like.dto;

import com.prgrms.team03linkbookbe.comment.entity.Comment;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeResponseDto {
    private Long id;

    private Folder folder;

    private User user;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static LikeResponseDto fromEntity(Comment comment) {
        return LikeResponseDto.builder()
                .id(comment.getId())
                .folder(comment.getFolder())
                .user(comment.getUser())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
