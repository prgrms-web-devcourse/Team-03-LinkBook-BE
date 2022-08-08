package com.prgrms.team03linkbookbe.folder.dto;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.user.dto.UserSimpleResponseDto;
import com.prgrms.team03linkbookbe.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
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
