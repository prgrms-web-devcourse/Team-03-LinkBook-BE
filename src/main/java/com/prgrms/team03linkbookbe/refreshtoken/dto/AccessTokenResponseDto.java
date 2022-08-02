package com.prgrms.team03linkbookbe.refreshtoken.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AccessTokenResponseDto {

    private final String accessToken;

}
