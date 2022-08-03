package com.prgrms.team03linkbookbe.user.dto;

import com.prgrms.team03linkbookbe.interest.dto.InterestDto;
import com.prgrms.team03linkbookbe.interest.entity.Interest;
import com.prgrms.team03linkbookbe.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateRequestDto {

    private String name;

    private String image;

    private String introduce;

    private List<InterestDto> interests;

    public User toEntity() {
        User user = User.builder()
            .name(name)
            .image(image)
            .introduce(introduce)
            .build();
        List<Interest> interestList = interests.stream().map(
            (interest) -> Interest.builder()
                .field(interest.getField())
                .user(user)
                .build()).collect(Collectors.toList());
        for (Interest interest : interestList) {
            user.addInterest(interest);
        }
        return user;
    }

}
