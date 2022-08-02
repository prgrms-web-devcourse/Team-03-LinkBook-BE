package com.prgrms.team03linkbookbe.comment.dto;

import com.prgrms.team03linkbookbe.comment.entity.Comment;
import com.prgrms.team03linkbookbe.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponseDto {
    private Long id;

    private List<Comment> children;

    private String content;

    private User user;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static CommentResponseDto fromEntity(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .children(comment.getChildren())
                .content(comment.getContent())
                .user(comment.getUser())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
