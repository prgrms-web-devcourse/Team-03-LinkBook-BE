package com.prgrms.team03linkbookbe.comment.dto;

import com.prgrms.team03linkbookbe.comment.entity.Comment;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.user.entity.User;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateCommentRequest {
    @Positive(message = "id는 양수로 입력해주세요.")
    @NotNull(message = "코멘트 아이디를 입력해주세요.")
    private Long id;

    @NotNull(message = "내용을 입력해주세요.")
    @Size(min = 1, max = 1000, message = "댓글은 1~1000자 까지 가능합니다.")
    private String content;

    @Positive(message = "id는 양수로 입력해주세요.")
    private Long folderId;

    @Positive(message = "id는 양수로 입력해주세요.")
    private Long userId;

    public static Comment toEntity(Folder folder, User user,
                                   Comment comment) {
        return Comment.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .folder(folder)
                .user(user)
                .build();
    }
}
