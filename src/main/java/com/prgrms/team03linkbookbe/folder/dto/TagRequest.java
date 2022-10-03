package com.prgrms.team03linkbookbe.folder.dto;

import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagRequest {

    private Long id;
    private String name;

}
