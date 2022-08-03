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

    private final UserLoginResponseDto userDetail;

    public static LoginResponseDto fromEntity(String accessToken, String refreshToken, User user, Boolean isFirstLogin) {
        UserLoginResponseDto userLoginResponseDto = UserLoginResponseDto.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .image(user.getImage())
            .build();

        return LoginResponseDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .isFirstLogin(isFirstLogin)
            .userDetail(userLoginResponseDto)
            .build();
    }
}
