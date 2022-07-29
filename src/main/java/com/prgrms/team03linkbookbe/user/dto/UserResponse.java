package com.prgrms.team03linkbookbe.user.dto;

import com.prgrms.team03linkbookbe.user.entity.User;
import javax.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserResponse {
    private Long id;

    private String name;

    private String image;

    @Builder
    public UserResponse(Long id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public static UserResponse fromEntity(User user){
        return UserResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .image(user.getImage())
            .build();
    }
}