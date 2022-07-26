package com.prgrms.team03linkbookbe.unit.repository;

import com.prgrms.team03linkbookbe.comment.entity.Comment;
import com.prgrms.team03linkbookbe.comment.repository.CommentRepository;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
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
    private FolderRepository folderRepository;

    @Autowired
    private UserRepository userRepository;

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
                .isMain(false)
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
    @DisplayName("해당 폴더에 코멘트를 등록 할 수 있다.")
    void INSERT_COMMENT_BY_ID_TEST() {
        // given
        Folder folderEntity = folderRepository.getReferenceById(folder.getId());
        Comment commentEntity = Comment.builder()
                .content("hello")
                .folder(folderEntity)
                .user(user2)
                .build();

        // when
        Comment save = commentRepository.save(commentEntity);

        // then
        assertThat(save.getContent()).isEqualTo("hello");
    }

    @Test
    @DisplayName("해당 아이디의 코멘트를 조회 할 수 있다.")
    void FIND_COMMENT_BY_ID_TEST() {
        // given
        Long id = comment.getId();

        // when
        Comment byId = commentRepository.getReferenceById(id);

        // then
        assertThat(byId.getContent()).isEqualTo(comment.getContent());
        assertThat(byId.getId()).isEqualTo(comment.getId());
    }

    @Test
    @DisplayName("해당 폴더의 코멘트들을 조회 할 수 있다.")
    void FIND_COMMENT_BY_FOLDER_TEST() {
        // given
        Long id = folder.getId();

        // when
        Folder byId = folderRepository.getReferenceById(id);

        // then
        assertThat(byId.getComments().size()).isEqualTo(folder.getComments().size());
        assertThat(byId.getComments().get(0).getId()).isEqualTo(folder.getComments().get(0).getId());
    }

    @Test
    @DisplayName("해당 유저의 코멘트들을 조회 할 수 있다.")
    void FIND_COMMENT_BY_USER_TEST() {
        // given
        Long id = user2.getId();

        // when
        User byId = userRepository.getReferenceById(id);

        // then
        assertThat(byId.getComments().size()).isEqualTo(folder.getComments().size());
        assertThat(byId.getComments().get(0).getId()).isEqualTo(folder.getComments().get(0).getId());
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

        // when
        commentRepository.delete(byId);

        // then
        comment = commentRepository.save(byId);
    }

    @Test
    @DisplayName("해당 폴더의 코멘트들을 삭제 할 수 있다.")
    void DELETE_COMMENTS_BY_FOLDER_TEST() {
        // given
        Long id = folder.getId();
        Folder referenceById = folderRepository.getReferenceById(id);

        // when
        for (Comment o : referenceById.getComments()) {
            commentRepository.delete(o);
        }

        // then
        assertThat(referenceById.getComments()).isEmpty();
    }
}