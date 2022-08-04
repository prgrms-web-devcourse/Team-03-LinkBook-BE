package com.prgrms.team03linkbookbe.user.dto;

import com.prgrms.team03linkbookbe.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserSimpleResponseDto {

    private Long id;

    private String email;

    private String name;

    private String image;

    private String introduce;

    public static UserSimpleResponseDto fromEntity(User user) {
        return UserSimpleResponseDto.builder()
            .id(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .image(user.getImage())
            .introduce(user.getIntroduce())
            .build();
    }
}