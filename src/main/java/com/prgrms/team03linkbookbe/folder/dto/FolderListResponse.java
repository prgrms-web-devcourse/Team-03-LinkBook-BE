package com.prgrms.team03linkbookbe.folder.dto;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.user.dto.UserResponse;
import com.prgrms.team03linkbookbe.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FolderListResponse {

    private UserResponse userResponse;

    private List<FolderResponse> folders;


    public static FolderListResponse fromEntity(User user, List<Folder> folders){
        return FolderListResponse.builder()
            .userResponse(UserResponse.fromEntity(user))
            .folders(folders.stream().map(FolderResponse::fromEntity).collect(Collectors.toList()))
            .build();
    }
}
