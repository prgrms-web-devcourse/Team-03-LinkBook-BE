package com.prgrms.team03linkbookbe.unit.like.controller;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.like.controller.LikeController;
import com.prgrms.team03linkbookbe.like.dto.CreateLikeRequestDto;
import com.prgrms.team03linkbookbe.like.dto.CreateLikeResponseDto;
import com.prgrms.team03linkbookbe.like.service.LikeService;
import com.prgrms.team03linkbookbe.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class LikeControllerTest {
    @InjectMocks
    LikeController likeController;

    @Mock
    LikeService likeService;

    Folder folder;

    User user;

    @BeforeEach
    void setup() {
        folder = Folder.builder().build();

        user = User.builder().id(1L).build();
    }

    @Test
    @DisplayName("댓글 작성 테스트")
    void INSERT_COMMENT_TEST() {
        // given
        CreateLikeRequestDto requestDto = CreateLikeRequestDto.builder()
                .folderId(1L)
                .userId(1L)
                .build();

        when(likeService.create(requestDto, 1L))
                .thenReturn(CreateLikeResponseDto.builder().id(1L).build());

        // when, then
        assertThat(likeController.create(requestDto, user).getBody().getId())
                .isEqualTo(1L);
    }

    @Test
    @DisplayName("특정 댓글 삭제 테스트")
    void DELETE_COMMENT_TEST() {
        // given
        when(likeService.delete(1L, 1L)).thenReturn(null);

        // when, then
        assertThat(likeController.delete(1L, user).getBody()).isEqualTo(1L);
    }
}
