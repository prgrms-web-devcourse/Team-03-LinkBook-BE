package com.prgrms.team03linkbookbe.comment.dto;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentListResponse {
    private List<CommentResponse> comments;

    private Boolean isPrivate;
}
