package com.prgrms.team03linkbookbe.folder.dto;

import com.prgrms.team03linkbookbe.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OriginUserResponse {
    private Long id;
    private String name;

    public static OriginUserResponse fromEntity(User user) {
        return OriginUserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
