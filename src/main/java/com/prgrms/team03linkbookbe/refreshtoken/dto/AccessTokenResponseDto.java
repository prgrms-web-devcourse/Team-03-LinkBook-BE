package com.prgrms.team03linkbookbe.refreshtoken.dto;

import com.prgrms.team03linkbookbe.user.dto.UserSimpleResponseDto;
import com.prgrms.team03linkbookbe.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AccessTokenResponseDto {

    private final String accessToken;

    private final UserSimpleResponseDto user;

    public static AccessTokenResponseDto fromEntity(String accessToken, User user) {
        return AccessTokenResponseDto.builder()
            .accessToken(accessToken)
            .user(UserSimpleResponseDto.fromEntity(user))
            .build();
    }
}
