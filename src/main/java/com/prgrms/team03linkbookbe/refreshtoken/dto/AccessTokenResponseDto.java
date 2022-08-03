package com.prgrms.team03linkbookbe.refreshtoken.dto;

import com.prgrms.team03linkbookbe.user.dto.UserLoginResponseDto;
import com.prgrms.team03linkbookbe.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AccessTokenResponseDto {

    private final String accessToken;

    private final UserLoginResponseDto userDetail;

    public static AccessTokenResponseDto fromEntity(String accessToken, User user) {
        UserLoginResponseDto userLoginResponseDto = UserLoginResponseDto.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .image(user.getImage())
            .build();

        return AccessTokenResponseDto.builder()
            .accessToken(accessToken)
            .userDetail(userLoginResponseDto)
            .build();

    }

}
