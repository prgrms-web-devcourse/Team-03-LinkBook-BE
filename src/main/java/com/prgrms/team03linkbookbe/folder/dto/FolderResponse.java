package com.prgrms.team03linkbookbe.folder.dto;

import com.prgrms.team03linkbookbe.bookmark.dto.BookmarkResponse;
import com.prgrms.team03linkbookbe.bookmark.entity.Bookmark;
import com.prgrms.team03linkbookbe.comment.dto.CommentResponseDto;
import com.prgrms.team03linkbookbe.comment.entity.Comment;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.user.dto.UserResponse;
import com.prgrms.team03linkbookbe.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FolderResponse {
    private Long id;

    private String name;

    private String image;

    private String content;

    private Long originId;

    private UserResponse userResponse;

    private List<BookmarkResponse> bookmarkResponses;

    private Long likes;

    private List<CommentResponseDto> commentResponses;

    @Builder
    public FolderResponse(Long id, String name, String image, String content, Long originId,
        UserResponse userResponse,
        List<BookmarkResponse> bookmarkResponses, Long likes,
        List<CommentResponseDto> commentResponses) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.content = content;
        this.originId = originId;
        this.userResponse = userResponse;
        this.bookmarkResponses = bookmarkResponses;
        this.likes = likes;
        this.commentResponses = commentResponses;
    }


    public static FolderResponse fromEntity(Folder folder){
        return FolderResponse.builder()
            .id(folder.getId())
            .name(folder.getName())
            .image(folder.getImage())
            .content(folder.getContent())
            .originId(folder.getOriginId())
            .userResponse(UserResponse.fromEntity(folder.getUser()))
            .bookmarkResponses(folder.getBookmarks().stream().map(BookmarkResponse::fromEntity).collect(
                Collectors.toList()))
            .commentResponses(folder.getComments().stream().map(CommentResponseDto::fromEntity).collect(
                Collectors.toList()))
            .build();
    }

}
