package com.prgrms.team03linkbookbe.user.dto;

import com.prgrms.team03linkbookbe.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RegisterResponseDto {

    private Long userId;

    public static RegisterResponseDto fromEntity (User user) {
        return RegisterResponseDto.builder()
            .userId(user.getId())
            .build();
    }
}
