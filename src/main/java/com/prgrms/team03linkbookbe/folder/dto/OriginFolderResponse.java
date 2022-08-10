package com.prgrms.team03linkbookbe.folder.dto;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OriginFolderResponse {
    private Long id;
    private String title;
    private OriginUserResponse user;

    public static OriginFolderResponse fromEntity(Folder folder) {
        return OriginFolderResponse.builder()
                .id(folder.getId())
                .title(folder.getTitle())
                .user(OriginUserResponse.fromEntity(folder.getUser()))
                .build();
    }
}
