package com.prgrms.team03linkbookbe.unit.like.repository;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.like.entity.Like;
import com.prgrms.team03linkbookbe.like.repository.LikeRepository;
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
class LikeRepositoryTest {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    TestEntityManager em;

    User user;

    Folder folder;

    Like like;

    @BeforeEach
    void setup() {
        user = User.builder()
                .email("test1@kakao.com")
                .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
                .name("test1")
                .image("url")
                .role("USER")
                .build();
        em.persist(user);

        folder = Folder.builder()
                .title("my-folder")
                .image("url")
                .content("halo")
                .isPinned(false)
                .isPrivate(false)
                .user(user)
                .build();
        em.persist(folder);

        like = Like.builder()
                .folder(folder)
                .user(user)
                .build();
        em.persist(like);
    }

    @Test
    @DisplayName("특정 폴더에 좋아요를 할 수 있다.")
    void INSERT_LIKE_BY_ID_TEST() {
        // given
        Like likeEntity = Like.builder()
                .folder(folder)
                .user(user)
                .build();

        // when
        Like save = likeRepository.save(likeEntity);

        // then
        assertThat(save.getFolder().getId()).isEqualTo(folder.getId());
    }

    @Test
    @DisplayName("특정 폴더의 좋아요를 삭제 할 수 있다.")
    void DELETE_LIKE_BY_ID_TEST() {
        // given
        Long id = like.getId();
        Like byId = likeRepository.getReferenceById(id);

        // then
        likeRepository.delete(byId);
    }
}