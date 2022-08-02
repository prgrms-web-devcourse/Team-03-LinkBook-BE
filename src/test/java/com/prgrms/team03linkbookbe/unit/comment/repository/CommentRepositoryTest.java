package com.prgrms.team03linkbookbe.unit.comment.repository;

import com.prgrms.team03linkbookbe.comment.entity.Comment;
import com.prgrms.team03linkbookbe.comment.repository.CommentRepository;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    TestEntityManager em;

    User user1;

    User user2;

    Folder folder;

    Comment comment;

    @BeforeEach
    void setup() {
        user1 = User.builder()
                .email("test1@kakao.com")
                .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
                .name("test1")
                .image("url")
                .role("USER")
                .build();
        em.persist(user1);

        folder = Folder.builder()
                .name("my-folder")
                .image("url")
                .content("halo")
                .isPinned(false)
                .isPrivate(false)
                .user(user1)
                .build();
        em.persist(folder);

        user2 = User.builder()
                .email("test2@kakao.com")
                .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
                .name("test2")
                .image("url")
                .role("USER")
                .build();
        em.persist(user2);

        comment = Comment.builder()
                .content("hi")
                .folder(folder)
                .user(user2)
                .build();
        em.persist(comment);
    }

    @Test
    @DisplayName("특정 폴더에 코멘트를 등록 할 수 있다.")
    void INSERT_COMMENT_BY_ID_TEST() {
        // given
        Comment commentEntity = Comment.builder()
                .content("hello")
                .folder(folder)
                .user(user2)
                .build();

        // when
        Comment save = commentRepository.save(commentEntity);

        // then
        assertThat(save.getContent()).isEqualTo("hello");
    }

    @Test
    @DisplayName("특정 코멘트에 답글을 등록 할 수 있다.")
    void INSERT_REPLY_COMMENT_BY_ID_TEST() {
        // given
        Comment temp = Comment.builder()
                .parent(comment)
                .content("sup")
                .folder(folder)
                .user(user1)
                .build();

        // when
        commentRepository.save(temp);
        comment = comment.toBuilder()
                .children(
                        commentRepository.findAllByParentId(comment.getId())
                )
                .build();

        // then
        assertThat(temp.getId()).isEqualTo(comment.getChildren().get(0).getId());
    }

    @Test
    @DisplayName("해당 아이디의 코멘트를 수정 할 수 있다.")
    void UPDATE_COMMENT_BY_ID_TEST() {
        // given
        Long id = comment.getId();
        Comment byId = commentRepository.getReferenceById(id);

        // when
        byId.toBuilder().content("updated").build();
        Comment save = commentRepository.save(byId);

        // then
        assertThat(save.getContent()).isEqualTo(byId.getContent());
    }

    @Test
    @DisplayName("해당 아이디의 코멘트를 삭제 할 수 있다.")
    void DELETE_COMMENT_BY_ID_TEST() {
        // given
        Long id = comment.getId();
        Comment byId = commentRepository.getReferenceById(id);

        // then
        commentRepository.delete(byId);
    }
}