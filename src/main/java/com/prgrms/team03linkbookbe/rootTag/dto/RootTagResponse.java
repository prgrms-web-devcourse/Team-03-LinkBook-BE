package com.prgrms.team03linkbookbe.rootTag.dto;

import com.prgrms.team03linkbookbe.rootTag.entity.RootTagCategory;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RootTagResponse {

    private String rootTag;

    private List<String> subTags;

    public static RootTagResponse fromEntity(RootTagCategory root){
        return RootTagResponse.builder()
                    .rootTag(root.getViewName())
                    .subTags(Arrays.stream(root.getTags()).map(TagCategory::getViewName).collect(
                        Collectors.toList()))
                    .build();
    }

}
