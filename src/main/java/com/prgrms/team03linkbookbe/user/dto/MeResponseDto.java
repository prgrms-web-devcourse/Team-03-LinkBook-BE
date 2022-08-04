package com.prgrms.team03linkbookbe.user.dto;

import com.prgrms.team03linkbookbe.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MeResponseDto {

    UserDetailResponseDto user;

    public static MeResponseDto fromEntity(User user) {
        return MeResponseDto.builder()
            .user(UserDetailResponseDto.fromEntity(user))
            .build();
    }
}
