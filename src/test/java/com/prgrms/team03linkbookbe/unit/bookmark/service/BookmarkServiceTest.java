package com.prgrms.team03linkbookbe.unit.bookmark.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.prgrms.team03linkbookbe.bookmark.dto.BookmarkRequest;
import com.prgrms.team03linkbookbe.bookmark.entity.Bookmark;
import com.prgrms.team03linkbookbe.bookmark.repository.BookmarkRepository;
import com.prgrms.team03linkbookbe.bookmark.service.BookmarkService;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import com.prgrms.team03linkbookbe.user.entity.User;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.error.OptionalShouldContainInstanceOf;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @InjectMocks
    BookmarkService bookmarkService;

    @Mock
    BookmarkRepository bookmarkRepository;

    @Mock
    FolderRepository folderRepository;

    @Test
    @DisplayName("북마크를 생성할 수 있다.")
    void CREATE_BOOKMARK_TEST(){
        BookmarkRequest dto = BookmarkRequest.builder()
                                        .title("test")
                                        .url("test.com")
                                        .folderId(1L)
                                        .build();

        Folder folder = Folder.builder().id(1L).build();
        Bookmark bookmark = Bookmark.builder().id(1L).folder(folder).build();

        doReturn(Optional.of(folder))
            .when(folderRepository)
            .findById(any(Long.class));

        doReturn(bookmark)
            .when(bookmarkRepository)
            .save(any(Bookmark.class));

        bookmarkService.create(dto);

        verify(folderRepository, times(1)).findById(any(Long.class));
        verify(bookmarkRepository, times(1)).save(any(Bookmark.class));


    }

    @Test
    @DisplayName("북마크를 수정할 수 있다.")
    void UPDATE_BOOKMARK_TEST(){
        BookmarkRequest dto = BookmarkRequest.builder()
            .title("test")
            .url("test.com")
            .folderId(1L)
            .build();

        User user = User.builder().id(1L).email("test@gmail.com").build();
        Folder folder = Folder.builder().id(1L).user(user).build();
        Bookmark bookmark = Bookmark.builder().id(1L).folder(folder).build();

        doReturn(Optional.of(folder))
            .when(folderRepository)
            .findById(any(Long.class));

        doReturn(Optional.of(bookmark))
            .when(bookmarkRepository)
            .findById(any(Long.class));

        bookmarkService.update("test@gmail.com",1L, dto);

        verify(folderRepository, times(1)).findById(any(Long.class));
        verify(bookmarkRepository, times(1)).findById(any(Long.class));

    }

    @Test
    @DisplayName("북마크를 삭제할 수 있다.")
    void DELETE_BOOKMARK_TEST(){
        User user = User.builder().id(1L).email("test@gmail.com").build();
        Folder folder = Folder.builder().id(1L).user(user).build();
        Bookmark bookmark = Bookmark.builder().id(1L).folder(folder).build();

        doReturn(Optional.of(bookmark))
            .when(bookmarkRepository)
            .findById(any(Long.class));

        doNothing()
            .when(bookmarkRepository)
            .delete(any(Bookmark.class));

        bookmarkService.delete("test@gmail.com",1L);


        verify(bookmarkRepository, times(1)).findById(any(Long.class));
        verify(bookmarkRepository, times(1)).delete(any(Bookmark.class));


    }








}
