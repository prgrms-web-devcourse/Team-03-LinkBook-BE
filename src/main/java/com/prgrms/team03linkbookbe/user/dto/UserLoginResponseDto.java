package com.prgrms.team03linkbookbe.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserLoginResponseDto {

    private Long id;

    private String email;

    private String name;

    private String image;

}
