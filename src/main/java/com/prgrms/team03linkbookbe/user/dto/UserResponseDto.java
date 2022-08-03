package com.prgrms.team03linkbookbe.user.dto;

import com.prgrms.team03linkbookbe.interest.dto.InterestDto;
import com.prgrms.team03linkbookbe.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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

    private final String introduce;

    private final String role;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    private List<InterestDto> interestDtos;

    public static UserResponseDto fromEntity(User user) {
        return UserResponseDto.builder()
            .email(user.getEmail())
            .name(user.getName())
            .image(user.getImage())
            .introduce(user.getIntroduce())
            .role(user.getRole())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .interestDtos(user.getInterests().stream()
                .map((interest) -> InterestDto.builder()
                    .field(interest.getField())
                    .build())
                .collect(Collectors.toList()))
            .build();
    }

}
