package com.prgrms.team03linkbookbe.folder.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FolderIdResponse {
    private Long id;

    public static FolderIdResponse fromEntity(Long folderId){
        return FolderIdResponse.builder()
            .id(folderId)
            .build();
    }

}
