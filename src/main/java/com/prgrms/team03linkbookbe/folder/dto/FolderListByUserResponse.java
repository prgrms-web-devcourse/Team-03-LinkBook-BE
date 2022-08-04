package com.prgrms.team03linkbookbe.folder.dto;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.user.dto.UserResponse;
import com.prgrms.team03linkbookbe.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;


@Builder
@Getter
public class FolderListByUserResponse {

    private UserResponse userResponse;

    private Page<FolderResponse> folders;


    public static FolderListByUserResponse fromEntity(User user, Page<Folder> folders) {
        return FolderListByUserResponse.builder()
            .userResponse(UserResponse.fromEntity(user))
            .folders(folders.map(FolderResponse::fromEntity))
            .build();
    }
}
