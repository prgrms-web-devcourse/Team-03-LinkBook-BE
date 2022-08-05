package com.prgrms.team03linkbookbe.unit.bookmark.entity;
import com.prgrms.team03linkbookbe.bookmark.dto.BookmarkRequest;
import com.prgrms.team03linkbookbe.bookmark.entity.Bookmark;
import static org.assertj.core.api.Assertions.*;


import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BookmarkTest {

    private User user(){
        return User.builder()
            .id(1L)
            .name("test")
            .email("test@gmail.com")
            .password("1233")
            .role("USER")
            .build();
    }

    private Folder folder(){
        return Folder.builder()
            .id(1L)
            .user(user())
            .isPrivate(true)
            .isPinned(true)
            .content("test")
            .title("test")
            .originId(null)
            .image("image")
            .build();
    }


    @Test
    @DisplayName("북마크를 수정할 수 있다.")
    void MODIFY_BOOKMARK_TEST(){
        //given
        Bookmark bookmark = Bookmark.builder()
                                    .id(1L)
                                    .url("test.com")
                                    .title("test")
                                    .folder(folder())
                                    .build();

        BookmarkRequest dto = BookmarkRequest.builder()
                                    .url("test")
                                    .title("change")
                                    .build();


        assertThat(bookmark.getTitle()).isEqualTo("test");

        //when
        bookmark.modifyBookmark(dto,bookmark.getFolder());

        //then
        assertThat(bookmark.getTitle()).isEqualTo("change");

    }


}
