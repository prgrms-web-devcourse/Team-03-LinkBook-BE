package com.prgrms.team03linkbookbe.folder.dto;

import com.prgrms.team03linkbookbe.rootTag.entity.RootTagCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RootTagRequest {

    RootTagCategory tag;
}
