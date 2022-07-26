package com.prgrms.team03linkbookbe.user.dto;

import com.prgrms.team03linkbookbe.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDetailResponseDto {

    private final Long id;

    private final String email;

    private final String name;

    private final String image;

    private final String introduce;

    private final String role;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    private List<String> interests;

    public static UserDetailResponseDto fromEntity(User user) {
        return UserDetailResponseDto.builder()
            .id(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .image(user.getImage())
            .introduce(user.getIntroduce())
            .role(user.getRole())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .interests(user.getInterests().stream()
                .map(interest -> interest.getTag().getViewName())
                .collect(Collectors.toList()))
            .build();
    }

}
