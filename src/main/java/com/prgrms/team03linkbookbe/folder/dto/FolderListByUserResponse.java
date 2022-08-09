package com.prgrms.team03linkbookbe.folder.dto;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.like.entity.Like;
import com.prgrms.team03linkbookbe.user.dto.UserSimpleResponseDto;
import com.prgrms.team03linkbookbe.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;


@Builder
@Getter
public class FolderListByUserResponse {

    private UserSimpleResponseDto user;

    private Page<FolderResponse> folders;


    public static FolderListByUserResponse fromEntity(User user, Page<Folder> folders, List<Like> likes) {
        return FolderListByUserResponse.builder()
                .user(UserSimpleResponseDto.fromEntity(user))
                .folders(folders.map(f -> FolderResponse.fromEntity(f,
                        likes.stream().anyMatch(l -> l.getFolder().getId().equals(f.getId())))))
                .build();
    }
}
