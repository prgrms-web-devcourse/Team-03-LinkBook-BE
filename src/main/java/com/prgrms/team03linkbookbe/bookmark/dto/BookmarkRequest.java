package com.prgrms.team03linkbookbe.bookmark.dto;

import com.prgrms.team03linkbookbe.bookmark.entity.Bookmark;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkRequest {

    private Long id;

    @NotBlank(message = "url을 입력하세요")
    private String url;

    @NotBlank(message = "북마크이름을 입력하세요")
    private String title;

    private Long folderId;

    public Bookmark toEntity(Folder folder){
        return Bookmark.builder()
            .url(this.url)
            .title(this.title)
            .folder(folder)
            .build();
    }

}
