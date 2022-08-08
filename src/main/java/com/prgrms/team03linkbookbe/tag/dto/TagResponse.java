package com.prgrms.team03linkbookbe.tag.dto;

import com.prgrms.team03linkbookbe.tag.entity.Tag;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TagResponse {

    private List<String> tags;

    public static TagResponse fromEntity(List<Tag> tags){
        return TagResponse.builder()
            .tags(tags.stream().map(tag -> tag.getName().getViewName()).collect(Collectors.toList()))
            .build();
    }


}
