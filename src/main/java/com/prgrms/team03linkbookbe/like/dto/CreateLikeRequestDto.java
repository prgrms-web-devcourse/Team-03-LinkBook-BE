package com.prgrms.team03linkbookbe.like.dto;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.like.entity.Like;
import com.prgrms.team03linkbookbe.user.entity.User;
import lombok.*;

import javax.validation.constraints.Positive;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateLikeRequestDto {
    @Positive(message = "id는 양수로 입력해주세요.")
    private Long folderId;

    @Positive(message = "id는 양수로 입력해주세요.")
    private Long userId;

    public static Like toEntity(Folder folder, User user) {
        return Like.builder()
                .folder(folder)
                .user(user)
                .build();
    }
}
