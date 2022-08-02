package com.prgrms.team03linkbookbe.user.dto;

import com.prgrms.team03linkbookbe.interest.entity.Interest;
import java.util.List;
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

    private List<Interest> interests;

}
