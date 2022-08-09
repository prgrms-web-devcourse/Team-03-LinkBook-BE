package com.prgrms.team03linkbookbe.folder.dto;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Builder
@Getter
public class FolderListResponse {

    private Page<FolderResponse> folders;

    public static FolderListResponse fromEntity(Page<Folder> folders) {
        return FolderListResponse.builder()
            .folders(folders.map(FolderResponse::fromEntity))
            .build();
    }
}
