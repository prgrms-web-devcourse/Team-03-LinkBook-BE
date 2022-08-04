package com.prgrms.team03linkbookbe.interest.dto;

import com.prgrms.team03linkbookbe.interest.entity.Field;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterestDto {

    private String field;

}
