package com.prgrms.team03linkbookbe.folder.dto;

import com.prgrms.team03linkbookbe.bookmark.dto.BookmarkResponse;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.user.dto.UserSimpleResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PinnedResponse {

    private Long id;

    private String title;

    private String image;

    private UserSimpleResponseDto user;

    private List<BookmarkResponse> bookmarks;

    private List<String> tags;

    private LocalDateTime createdAt;

    public static PinnedResponse fromEntity(Folder folder) {
        return PinnedResponse.builder()
            .id(folder.getId())
            .title(folder.getTitle())
            .image(folder.getImage())
            .user(UserSimpleResponseDto.fromEntity(folder.getUser()))
            .bookmarks(
                folder.getBookmarks().stream().map(BookmarkResponse::fromEntity)
                    .collect(Collectors.toList())
            )
            .tags(
                folder.getFolderTags().stream().map(folderTag ->
                    folderTag.getTag().getName().getViewName()).collect(Collectors.toList())
            )
            .createdAt(folder.getCreatedAt())
            .build();
    }
}
