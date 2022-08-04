package com.prgrms.team03linkbookbe.user.dto;

import com.prgrms.team03linkbookbe.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponseDto {

    private final String accessToken;

    private final String refreshToken;

    private final Boolean isFirstLogin;

    private final UserSimpleResponseDto user;

    public static LoginResponseDto fromEntity(String accessToken, String refreshToken, User user,
        Boolean isFirstLogin) {
        return LoginResponseDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .isFirstLogin(isFirstLogin)
            .user(UserSimpleResponseDto.fromEntity(user))
            .build();
    }
}
