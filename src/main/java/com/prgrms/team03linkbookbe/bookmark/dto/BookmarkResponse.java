package com.prgrms.team03linkbookbe.bookmark.dto;

import com.prgrms.team03linkbookbe.bookmark.entity.Bookmark;
import java.awt.print.Book;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkResponse {

    private Long id;

    private String url;

    private String title;


    public static BookmarkResponse fromEntity(Bookmark bookmark){
        return BookmarkResponse.builder()
            .id(bookmark.getId())
            .url(bookmark.getUrl())
            .title(bookmark.getTitle())
            .build();
    }
}
