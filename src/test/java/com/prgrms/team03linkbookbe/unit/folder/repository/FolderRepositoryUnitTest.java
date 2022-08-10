package com.prgrms.team03linkbookbe.unit.folder.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import com.prgrms.team03linkbookbe.folderTag.entity.FolderTag;
import com.prgrms.team03linkbookbe.rootTag.entity.RootTag;
import com.prgrms.team03linkbookbe.rootTag.entity.RootTagCategory;
import com.prgrms.team03linkbookbe.tag.entity.Tag;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import com.prgrms.team03linkbookbe.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
public class FolderRepositoryUnitTest {

    @Autowired
    FolderRepository folderRepository;

    @Autowired
    TestEntityManager em;

    Folder folder;

    User user;

    RootTag rootTag;

    Tag tag;

    FolderTag folderTag;

    Pageable pageable = PageRequest.of(0, 5);

    @BeforeEach
    void setup() {
        // given
        user = User.builder()
            .email("user@gmail.com")
            .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
            .name("username")
            .introduce("introduce")
            .image("https:/")
            .role("ROLE_USER")
            .build();

        em.persist(user);

        folder = Folder.builder()
            .user(user)
            .isPrivate(false)
            .likes(0)
            .isPinned(false)
            .image("폴더이미지URL")
            .title("제목")
            .content("내용")
            .build();
    }

    @Test
    @DisplayName("폴더 생성 테스트")
    void CREATE_FOLDER_TEST() {
        // when
        Folder receivedFolder = folderRepository.save(folder);

        // then
        assertThat(receivedFolder.getTitle()).isEqualTo("제목");
    }

    @Test
    @DisplayName("폴더 조회 테스트")
    void READ_FOLDER_TEST() {
        // given
        Folder receivedFolder = folderRepository.save(folder);

        // when
        Folder readFolder = folderRepository.findById(receivedFolder.getId()).get();

        // then
        assertThat(readFolder.getTitle()).isEqualTo("제목");
    }

    @Test
    @DisplayName("폴더 삭제 테스트")
    void UPDATE_FOLDER_TEST() {
        // given
        Folder receivedFolder = folderRepository.save(folder);

        // when
        folderRepository.deleteById(receivedFolder.getId());

        // then
        assertThat(folderRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("페이지네이션 폴더 조회 테스트")
    void READ_FOLDERS_TEST() {
        // given
        Folder receivedFolder = folderRepository.save(folder);

        // when
        Page<Folder> folders = folderRepository.findAll(false, pageable);

        // then
        assertThat(folders.getTotalElements()).isEqualTo(1L);
    }

    @Test
    @DisplayName("페이지네이션 사용자 폴더 조회 테스트")
    void READ_FOLDERS_BY_EMAIL_TEST() {
        // given
        Folder receivedFolder = folderRepository.save(folder);

        // when
        Page<Folder> folders = folderRepository.findAllByUser(user, false, pageable);

        // then
        assertThat(folders.getTotalElements()).isEqualTo(1L);
    }

    @Test
    @DisplayName("페치 조인 특정 폴더 조회 테스트")
    void READ_FOLDER_WITH_FETCH_JOIN_TEST() {
        // given
        Folder receivedFolder = folderRepository.save(folder);

        // when
        Folder folder = folderRepository.findByIdWithFetchJoin(receivedFolder.getId()).get(0);

        // then
        assertThat(folder.getUser().getName()).isEqualTo("username");
    }

    @Test
    @DisplayName("제목으로 폴더 검색 테스트")
    void READ_FOLDER_BY_TITLE_TEST() {
        // given
        Folder receivedFolder = folderRepository.save(folder);

        // when
        Page<Folder> folders = folderRepository.findAllByTitle(false, pageable, "제목");

        // then
        assertThat(folders.getTotalElements()).isEqualTo(1L);
    }

    @Test
    @DisplayName("루트 태그로 폴더 검색 테스트")
    void READ_FOLDERS_BY_ROOT_TAG_TEST() {
        // given
        rootTag = RootTag.builder()
            .name(RootTagCategory.DEVELOP)
            .build();

        em.persist(rootTag);

        tag = Tag.builder()
            .rootTag(rootTag)
            .name(TagCategory.DEVELOP1)
            .build();

        em.persist(tag);

        Folder receivedFolder = folderRepository.save(folder);

        folderTag = FolderTag.builder()
            .tag(tag)
            .folder(receivedFolder)
            .build();

        em.persist(folderTag);

        em.flush();

        // when
        Page<Folder> folders = folderRepository.findByRootTag(RootTagCategory.DEVELOP, pageable);

        // then
        assertThat(folders.getTotalElements()).isEqualTo(1L);
    }

    @Test
    @DisplayName("태그로 폴더 검색 테스트")
    void READ_FOLDERS_BY_TAG_TEST() {
        // given
        rootTag = RootTag.builder()
            .name(RootTagCategory.DEVELOP)
            .build();

        em.persist(rootTag);

        tag = Tag.builder()
            .rootTag(rootTag)
            .name(TagCategory.DEVELOP1)
            .build();

        em.persist(tag);

        Folder receivedFolder = folderRepository.save(folder);

        folderTag = FolderTag.builder()
            .tag(tag)
            .folder(receivedFolder)
            .build();

        em.persist(folderTag);

        em.flush();

        // when
        Page<Folder> folders = folderRepository.findByTag(TagCategory.DEVELOP1, pageable);

        // then
        assertThat(folders.getTotalElements()).isEqualTo(1L);
    }
}
