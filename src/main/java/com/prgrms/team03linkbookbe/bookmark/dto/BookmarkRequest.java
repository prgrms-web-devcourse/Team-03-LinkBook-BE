package com.prgrms.team03linkbookbe.bookmark.dto;

import com.prgrms.team03linkbookbe.bookmark.entity.Bookmark;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
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

    private String url;

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
