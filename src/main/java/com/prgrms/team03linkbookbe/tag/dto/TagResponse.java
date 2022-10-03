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

    private Long id;

    private String name;

    public static TagResponse fromEntity(Tag tag){
        return TagResponse.builder()
            .id(tag.getId())
            .name(tag.getName())
            .build();
    }


}
