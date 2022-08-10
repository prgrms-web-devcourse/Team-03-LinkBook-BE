package com.prgrms.team03linkbookbe.unit.like.controller;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.jwt.JwtAuthentication;
import com.prgrms.team03linkbookbe.like.controller.LikeController;
import com.prgrms.team03linkbookbe.like.dto.CreateLikeRequest;
import com.prgrms.team03linkbookbe.like.dto.CreateLikeResponse;
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
    JwtAuthentication jwtAuthentication;

    @BeforeEach
    void setup() {
        folder = Folder.builder().build();

        String email = "test@test.com";
        String password = "test1234!";

        user = User.builder()
                .id(1L)
                .email(email)
                .password(password)
                .build();

        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        String refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IklsaHdhbiBMZWUiLCJpYXQiOjE1MTYyMzkwMjJ9.HjKjCVRYo5kZH1tDbFzh5HLYwEB6WTqdbFIQLTWLA6U";
        jwtAuthentication =
                new JwtAuthentication(accessToken, refreshToken, email);
    }

    @Test
    @DisplayName("댓글 작성 테스트")
    void INSERT_COMMENT_TEST() {
        // given
        CreateLikeRequest requestDto = CreateLikeRequest.builder()
                .folderId(1L)
                .userId(1L)
                .build();

        when(likeService.create(requestDto, user.getEmail()))
                .thenReturn(CreateLikeResponse.builder().id(1L).build());

        // when, then
        assertThat(likeController.create(requestDto, jwtAuthentication).getBody().getId())
                .isEqualTo(1L);
    }

    @Test
    @DisplayName("특정 댓글 삭제 테스트")
    void DELETE_COMMENT_TEST() {
        // given
        when(likeService.delete(1L, user.getEmail())).thenReturn(null);

        // when, then
        likeController.delete(1L, jwtAuthentication);
    }
}
