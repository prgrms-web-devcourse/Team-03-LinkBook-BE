package com.prgrms.team03linkbookbe.unit.folder.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.prgrms.team03linkbookbe.bookmark.repository.BookmarkRepository;
import com.prgrms.team03linkbookbe.folder.dto.CreateFolderRequest;
import com.prgrms.team03linkbookbe.folder.dto.FolderDetailResponse;
import com.prgrms.team03linkbookbe.folder.dto.FolderIdResponse;
import com.prgrms.team03linkbookbe.folder.dto.FolderListByUserResponse;
import com.prgrms.team03linkbookbe.folder.dto.FolderListResponse;
import com.prgrms.team03linkbookbe.folder.dto.RootTagRequest;
import com.prgrms.team03linkbookbe.folder.dto.TagRequest;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import com.prgrms.team03linkbookbe.folder.service.FolderService;
import com.prgrms.team03linkbookbe.folderTag.service.FolderTagService;
import com.prgrms.team03linkbookbe.jwt.JwtAuthentication;
import com.prgrms.team03linkbookbe.like.repository.LikeRepository;
import com.prgrms.team03linkbookbe.rootTag.entity.RootTagCategory;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class FolderServiceUnitTest {

    @InjectMocks
    FolderService folderService;

    @Mock
    FolderRepository folderRepository;

    @Mock
    FolderTagService folderTagService;

    @Mock
    UserRepository userRepository;

    @Mock
    BookmarkRepository bookmarkRepository;

    @Mock
    LikeRepository likeRepository;

    String email = "example1@gmail.com";

    Pageable pageable = PageRequest.of(0, 5);

    User user = User.builder()
        .id(1L)
        .email(email)
        .name("이름")
        .image("이미지URL")
        .introduce("소갯말")
        .build();

    @Test
    @Disabled
    @DisplayName("폴더 생성 테스트")
    void CREATE_FOLDER_TEST() {
        // given
        JwtAuthentication authentication =
            new JwtAuthentication("at", "rt", email);

        CreateFolderRequest request = CreateFolderRequest.builder()
            .isPrivate(false)
            .title("제목")
            .content("내용")
            .image("이미지URL")
            .isPinned(false)
            .bookmarks(Lists.emptyList())
            .tags(Lists.emptyList())
            .build();

        when(userRepository.findByEmail(email)).thenReturn(
            Optional.of(user));

        when(folderRepository.save(any(Folder.class))).thenReturn(
            Folder.builder()
                .id(1L)
                .build()
        );

        // when
        FolderIdResponse responseDto = folderService.create(authentication, request);

        // then
        assertThat(responseDto.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("폴더 전체 조회 테스트(페이지네이션)")
    void GET_ALL_TEST() {
        // given
        JwtAuthentication authentication =
            new JwtAuthentication("at", "rt", email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        when(likeRepository.findAllByUser(any(User.class))).thenReturn(Lists.emptyList());

        Page<Folder> folders = new PageImpl<>(List.of(Folder.builder().id(1L).user(user).build()));

        when(folderRepository.findAll(false, pageable, user)).thenReturn(folders);

        // when
        FolderListResponse response = folderService.getAll(pageable, authentication);

        // then
        assertThat(response.getFolders().getTotalElements()).isEqualTo(1L);
    }

    @Test
    @DisplayName("폴더 자세히 조회 테스트")
    void DETAIL_TEST() {
        // given
        JwtAuthentication authentication =
            new JwtAuthentication("at", "rt", email);

        when(folderRepository.findByIdWithFetchJoin(1L)).thenReturn(
            List.of(
                Folder.builder()
                    .id(1L)
                    .user(user)
                    .build()
            )
        );

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        when(likeRepository.findAllByUser(any(User.class))).thenReturn(Lists.emptyList());

        // when
        FolderDetailResponse response = folderService.detail(1L, authentication);

        // then
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("특정 사용자 폴더 조회 테스트")
    void GET_ALL_BY_USER_TEST() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(likeRepository.findAllByUser(any(User.class))).thenReturn(Lists.emptyList());

        Page<Folder> folders = new PageImpl<>(
            List.of(
                Folder.builder()
                    .id(1L)
                    .user(user)
                    .build()
            )
        );

        when(folderRepository.findAllByUser(user, false, pageable)).thenReturn(folders);

        // when
        FolderListByUserResponse response
            = folderService.getAllByUser(1L, null, "false", pageable);

        // then
        assertThat(response.getFolders().getTotalElements()).isEqualTo(1L);
    }

    @Test
    @DisplayName("제목으로 검색 테스트")
    void FIND_ALL_BY_TITLE_TEST() {
        JwtAuthentication authentication =
            new JwtAuthentication("at", "rt", email);

        // given
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        when(likeRepository.findAllByUser(any(User.class))).thenReturn(Lists.emptyList());

        Page<Folder> folders = new PageImpl<>(
            List.of(
                Folder.builder()
                    .id(1L)
                    .title("제목")
                    .user(user)
                    .build()
            )
        );

        when(folderRepository.findAllByTitle(false, pageable, "제목", user))
            .thenReturn(folders);

        // when
        FolderListResponse response
            = folderService.getAllByTitle(pageable, "제목", authentication);

        // then
        assertThat(response.getFolders().getTotalElements()).isEqualTo(1L);
    }

    @Test
    @DisplayName("특정 폴더 수정 테스트")
    void UPDATE_TEST() {
        // given
        Folder folder = Folder.builder()
            .id(1L)
            .user(user)
            .build();

        when(folderRepository.findById(1L)).thenReturn(Optional.of(folder));

        doNothing().when(bookmarkRepository).deleteAllByFolder(folder);

        doNothing().when(folderTagService).addFolderTag(any(CreateFolderRequest.class), eq(folder));

        CreateFolderRequest createFolderRequest = CreateFolderRequest.builder()
            .title("제목수정")
            .bookmarks(Lists.emptyList())
            .build();

        // when
        FolderIdResponse response
            = folderService.update(email, 1L, createFolderRequest);

        // then
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("특정 폴더 삭제 테스트")
    void DELETE_TEST() {
        // given
        Folder folder = Folder.builder()
            .id(1L)
            .user(user)
            .build();

        when(folderRepository.findById(1L)).thenReturn(Optional.of(folder));

        // when & then
        folderService.delete(email, 1L);
    }

    @Test
    @DisplayName("루트태그로 검색 테스트")
    void FIND_BY_ROOT_TAG_TEST() {
        // given
        RootTagRequest rootTagRequest = RootTagRequest.builder()
            .rootTag(RootTagCategory.DEVELOP)
            .build();

        JwtAuthentication authentication =
            new JwtAuthentication("at", "rt", email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        when(likeRepository.findAllByUser(any(User.class))).thenReturn(Lists.emptyList());

        Page<Folder> folders = new PageImpl<>(
            List.of(
                Folder.builder()
                    .id(1L)
                    .title("제목")
                    .user(user)
                    .build()
            )
        );

        when(folderRepository.findByRootTag(RootTagCategory.DEVELOP, pageable))
            .thenReturn(folders);

        // when
        FolderListResponse response
            = folderService.getByRootTag(rootTagRequest, pageable, authentication);

        // then
        assertThat(response.getFolders().getTotalElements()).isEqualTo(1L);
    }

    @Test
    @DisplayName("태그로 검색 테스트")
    void FIND_BY_TAG_TEST() {
        // given
        TagRequest tagRequest = TagRequest.builder()
            .tag(TagCategory.DEVELOP1)
            .build();

        JwtAuthentication authentication =
            new JwtAuthentication("at", "rt", email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        when(likeRepository.findAllByUser(any(User.class))).thenReturn(Lists.emptyList());

        Page<Folder> folders = new PageImpl<>(
            List.of(
                Folder.builder()
                    .id(1L)
                    .title("제목")
                    .user(user)
                    .build()
            )
        );

        when(folderRepository.findByTag(TagCategory.DEVELOP1, pageable))
            .thenReturn(folders);

        // when
        FolderListResponse response
            = folderService.getByTag(tagRequest, pageable, authentication);

        // then
        assertThat(response.getFolders().getTotalElements()).isEqualTo(1L);
    }
}