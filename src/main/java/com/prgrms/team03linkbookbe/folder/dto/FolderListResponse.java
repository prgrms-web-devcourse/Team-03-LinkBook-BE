package com.prgrms.team03linkbookbe.folder.dto;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.like.entity.Like;
import com.prgrms.team03linkbookbe.user.dto.UserSimpleResponseDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
@Getter
public class FolderListResponse {

    private Page<FolderResponse> folders;


    public static FolderListResponse fromEntity(Page<Folder> folders, List<Like> likes) {
        return FolderListResponse.builder()
                .folders(folders.map(f -> FolderResponse.fromEntity(f,
                        likes.stream().anyMatch(l -> l.getFolder().getId().equals(f.getId())))))
                .build();
    }
}
