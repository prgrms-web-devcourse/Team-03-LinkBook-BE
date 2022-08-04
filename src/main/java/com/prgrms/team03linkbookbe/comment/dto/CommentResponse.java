package com.prgrms.team03linkbookbe.comment.dto;

import com.prgrms.team03linkbookbe.comment.entity.Comment;
import com.prgrms.team03linkbookbe.user.dto.UserSimpleResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponse {

    private Long id;

    private List<CommentResponse> children;

    private String content;

    private UserSimpleResponseDto user;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static CommentResponse fromEntity(Comment comment) {
        return CommentResponse.builder()
            .id(comment.getId())
            .children(comment.getChildren().stream()
                .map(CommentResponse::fromEntity)
                .collect(Collectors.toList()))
            .content(comment.getContent())
            .user(UserSimpleResponseDto.fromEntity(comment.getUser()))
            .createdAt(comment.getCreatedAt())
            .updatedAt(comment.getUpdatedAt())
            .build();
    }
}
