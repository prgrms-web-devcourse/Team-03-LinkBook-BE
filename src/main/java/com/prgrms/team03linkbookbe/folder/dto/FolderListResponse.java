package com.prgrms.team03linkbookbe.folder.dto;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.user.dto.UserResponse;
import com.prgrms.team03linkbookbe.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class FolderListResponse {

    private UserResponse userResponse;

    private List<FolderResponse> folders;


    public static FolderListResponse fromEntity(User user, List<Folder> folders) {
        return FolderListResponse.builder()
            .userResponse(UserResponse.fromEntity(user))
            .folders(folders.stream().map(FolderResponse::fromEntity).collect(Collectors.toList()))
            .build();
    }
}
