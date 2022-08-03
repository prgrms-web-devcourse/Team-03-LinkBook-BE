package com.prgrms.team03linkbookbe.unit.like.service;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import com.prgrms.team03linkbookbe.like.dto.CreateLikeRequest;
import com.prgrms.team03linkbookbe.like.entity.Like;
import com.prgrms.team03linkbookbe.like.repository.LikeRepository;
import com.prgrms.team03linkbookbe.like.service.LikeService;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class LikeServiceTest {
    @InjectMocks
    LikeService likeService;

    @Mock
    LikeRepository likeRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    FolderRepository folderRepository;
    CreateLikeRequest requestDto1;

    Folder folder;

    User user;

    @BeforeEach
    void setup() {
        requestDto1 = CreateLikeRequest.builder()
                .folderId(1L)
                .userId(1L)
                .build();

        folder = Folder.builder().build();

        String email = "test@test.com";
        String password = "test1234!";

        user = User.builder()
                .id(1L)
                .email(email)
                .password(password)
                .build();
    }

    @Test
    @DisplayName("좋아요 등록 테스트")
    void INSERT_COMMENT_TEST() {
        // given
        when(folderRepository.findById(1L)).thenReturn(Optional.of(folder));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Like like = CreateLikeRequest.toEntity(folder, user);
        like.toBuilder().id(1L).build();
        when(likeRepository.save(like)).thenReturn(like.toBuilder().id(1L).build());

        // when, then
        assertThat(likeService.create(requestDto1, user.getEmail()).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("좋아요 삭제 테스트")
    void DELETE_COMMENT_TEST() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(likeRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(Like.builder().id(1L).build()));

        // when
        Long deleteLike = likeService.delete(1L, user.getEmail());

        // then
        assertThat(deleteLike).isEqualTo(1L);
    }
}
