package com.prgrms.team03linkbookbe.rootTag.dto;

import com.prgrms.team03linkbookbe.rootTag.entity.RootTagCategory;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RootTagListResponse {

    private List<RootTagResponse> tags;

    public static RootTagListResponse fromEntity(RootTagCategory[] roots){
        return RootTagListResponse.builder()
                        .tags(Arrays.stream(roots).map(RootTagResponse::fromEntity).collect(Collectors.toList()))
                        .build();
    }


}
