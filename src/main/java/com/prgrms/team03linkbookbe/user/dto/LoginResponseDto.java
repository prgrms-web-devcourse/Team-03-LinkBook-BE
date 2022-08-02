package com.prgrms.team03linkbookbe.user.dto;

import com.prgrms.team03linkbookbe.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponseDto {

    private final String accessToken;

    private final String refreshToken;

}
