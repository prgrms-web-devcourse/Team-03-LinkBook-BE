package com.prgrms.team03linkbookbe.unit.bookmark.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.team03linkbookbe.bookmark.dto.BookmarkRequest;
import com.prgrms.team03linkbookbe.bookmark.entity.Bookmark;
import com.prgrms.team03linkbookbe.bookmark.repository.BookmarkRepository;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.user.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@DataJpaTest // transaction 포함
@ExtendWith(SpringExtension.class)
class BookmarkRepositoryTest {

    final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    TestEntityManager em;

    User user;

    Folder folder;

    Bookmark bookmark;

    @BeforeEach
    void setup() {
        user = User.builder()
            .name("test")
            .email("test@gmail.com")
            .password("1233")
            .role("USER")
            .image("image")
            .build();

        em.persist(user);

        folder = Folder.builder()
            .user(user)
            .isPrivate(true)
            .isPinned(true)
            .content("test")
            .title("test")
            .originId(null)
            .image("image")
            .build();

        em.persist(folder);

        bookmark = Bookmark.builder()
            .folder(folder)
            .url("test.com")
            .title("test")
            .build();

        em.persist(bookmark);

    }


    @Test
    @DisplayName("북마크를 생성할 수 있다.")
    void CREATE_BOOKMARK_TEST() {
        Bookmark bookmark1 = Bookmark.builder()
            .folder(folder)
            .url("test.com")
            .title("test")
            .build();

        bookmarkRepository.save(bookmark1);
        List<Bookmark> all = bookmarkRepository.findAll();

        assertThat(all).hasSize(2);
    }


    @Test
    @DisplayName("북마크를 수정할 수 있다.")
    void UPDATE_BOOKMARK_TEST() {
        BookmarkRequest dto = BookmarkRequest.builder()
            .title("change")
            .url("test.com")
            .build();

        bookmark.modifyBookmark(dto, folder);
        // 업데이트쿼리 날리기
        em.flush();
        em.clear();

        Optional<Bookmark> find = bookmarkRepository.findById(bookmark.getId());
        assertThat(find).isPresent();
        assertThat(find.get().getTitle()).isEqualTo("change");
    }


    @Test
    @DisplayName("북마크를 삭제할 수 있다.")
    void DELETE_BOOKMARK_TEST() {
        bookmarkRepository.delete(bookmark);
        List<Bookmark> all = bookmarkRepository.findAll();

        assertThat(all).isEmpty();

    }

    @Test
    @DisplayName("북마크에 url과 title이 없으면 안된다.")
    void NO_URL_AND_TITLE_BOOKMARK_TEST() {
        Bookmark withoutUrl = Bookmark.builder()
            .folder(folder)
            .build();

        Set<ConstraintViolation<Bookmark>> validate = validator.validate(withoutUrl);
        assertThat(validate).hasSize(2);

    }


}
