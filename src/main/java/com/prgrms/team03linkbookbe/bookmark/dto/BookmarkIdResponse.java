package com.prgrms.team03linkbookbe.bookmark.dto;


import com.prgrms.team03linkbookbe.bookmark.entity.Bookmark;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkIdResponse {

    private Long id;

    public static BookmarkIdResponse fromEntity(Bookmark bookmark){
        return BookmarkIdResponse.builder()
            .id(bookmark.getId())
            .build();
    }

}
