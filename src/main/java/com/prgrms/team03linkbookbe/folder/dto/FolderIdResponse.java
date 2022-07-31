package com.prgrms.team03linkbookbe.folder.dto;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FolderIdResponse {

    private Long id;

    public static FolderIdResponse fromEntity(Long folderId) {
        return FolderIdResponse.builder()
            .id(folderId)
            .build();
    }

}
