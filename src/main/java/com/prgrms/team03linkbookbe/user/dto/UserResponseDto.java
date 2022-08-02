package com.prgrms.team03linkbookbe.user.dto;

import com.prgrms.team03linkbookbe.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Builder
@Getter
public class UserResponseDto {

    private final String email;

    private final String name;

    private final String image;

    private final String role;

    public static UserResponseDto fromEntity (User user) {
        return UserResponseDto.builder()
            .email(user.getEmail())
            .name(user.getName())
            .image(user.getImage())
            .role(user.getRole())
            .build();
    }

}
