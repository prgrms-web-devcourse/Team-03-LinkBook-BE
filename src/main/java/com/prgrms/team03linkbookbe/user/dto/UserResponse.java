package com.prgrms.team03linkbookbe.user.dto;

import com.prgrms.team03linkbookbe.user.entity.User;
import javax.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
public class UserResponse {
    private Long id;

    private String name;

    private String image;


    public static UserResponse fromEntity(User user){
        return UserResponse.builder()
            .id(user.getId())
            .name(user.getName())
            .image(user.getImage())
            .build();
    }
}
