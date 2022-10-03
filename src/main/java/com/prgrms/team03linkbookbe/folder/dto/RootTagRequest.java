package com.prgrms.team03linkbookbe.folder.dto;

import com.prgrms.team03linkbookbe.rootTag.entity.RootTagCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RootTagRequest {

    String name;

}
