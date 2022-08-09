package com.prgrms.team03linkbookbe.folder.dto;

import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagRequest {

    TagCategory tag;
}
