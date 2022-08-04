package com.prgrms.team03linkbookbe.folder.dto;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.user.dto.UserSimpleResponseDto;
import com.prgrms.team03linkbookbe.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class FolderListResponse {

    private UserSimpleResponseDto user;

    private List<FolderResponse> folders;


    public static FolderListResponse fromEntity(User user, List<Folder> folders) {
        return FolderListResponse.builder()
            .user(UserSimpleResponseDto.fromEntity(user))
            .folders(folders.stream().map(FolderResponse::fromEntity).collect(Collectors.toList()))
            .build();
    }
}
