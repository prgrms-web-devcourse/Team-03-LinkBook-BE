package com.prgrms.team03linkbookbe.unit.refreshtoken.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.prgrms.team03linkbookbe.jwt.Jwt;
import com.prgrms.team03linkbookbe.jwt.Jwt.Claims;
import com.prgrms.team03linkbookbe.jwt.exception.IllegalTokenException;
import com.prgrms.team03linkbookbe.refreshtoken.dto.AccessTokenResponseDto;
import com.prgrms.team03linkbookbe.refreshtoken.entity.RefreshToken;
import com.prgrms.team03linkbookbe.refreshtoken.repository.RefreshTokenRepository;
import com.prgrms.team03linkbookbe.refreshtoken.service.RefreshTokenService;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {

    @InjectMocks
    RefreshTokenService refreshTokenService;

    @Mock
    Jwt jwt;

    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @Mock
    UserRepository userRepository;

    @DisplayName("reissueAccessToken 메서드 : ")
    @Nested
    class ReissueAccessToken {
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        String newAccessToken = "newAccessToken";
        String email = "user@gmail.com";
        String[] roles = {"USER"};
        String password = "$2a$10$VgKG4LYAucDmh0PoSxjD6OaW8ADf7VUMf5ysPsQr5vh1QoVI7yXu6";
        User user = User.builder()
            .id(1L)
            .email(email)
            .password(password)
            .build();
        RefreshToken refreshTokenEntity = RefreshToken.builder()
            .id(1L)
            .token(refreshToken)
            .user(user)
            .build();


        @Test
        @DisplayName("RefreshToken을 이용하여 AccessToken을 재생성 할 수 있다.")
        void REISSUE_ACCESS_TOKEN() {
            // Given
            Claims claims = Claims.from(email, roles);
            given(jwt.isExpiredToken(accessToken)).willReturn(false);
            given(jwt.verify(refreshToken)).willReturn(claims);
            given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
            given(refreshTokenRepository.findByUserId(user.getId())).willReturn(Optional.of(refreshTokenEntity));
            given(jwt.createAccessToken(claims)).willReturn(newAccessToken);

            // When
            AccessTokenResponseDto responseDto = refreshTokenService.reissueAccessToken(
                accessToken, refreshToken);

            // Then
            assertThat(responseDto.getAccessToken()).isEqualTo(newAccessToken);
        }

        @Test
        @DisplayName("만료되지 않은 AccessToken을 가지고 있으면 AccessToken 재생성 할 수 없다.")
        void IS_EXPIRED_ACCESS_TOKEN() {
            // Given
            Claims claims = Claims.from(email, roles);
            given(jwt.isExpiredToken(accessToken)).willReturn(true);

            // When Then
            assertThatThrownBy(() -> refreshTokenService.reissueAccessToken(
                accessToken, refreshToken))
                .isInstanceOf(IllegalTokenException.class);
        }

        @Test
        @DisplayName("유효성 검증에 실패한 RefreshToken으로 AccessToken 재생성 할 수 없다.")
        void NO_VERIFIED_REFRESH_TOKEN() {
            // Given
            Claims claims = Claims.from(email, roles);
            given(jwt.isExpiredToken(accessToken)).willReturn(false);
            given(jwt.verify(refreshToken)).willThrow(new JWTVerificationException("유효성 검사 실패"));

            // When Then
            assertThatThrownBy(() -> refreshTokenService.reissueAccessToken(
                accessToken, refreshToken))
                .isInstanceOf(IllegalTokenException.class);
        }

        @Test
        @DisplayName("DB에 저장되어 있는 RefreshToken 값과 일치하지 않으면 AccessToken을 재생성 할 수 없다.")
        void NO_EQUAL_DB_REFRESHTOKEN() {
            // Given
            String inputRefreshToken = "inputRefreshToken";
            Claims claims = Claims.from(email, roles);
            given(jwt.isExpiredToken(accessToken)).willReturn(false);
            given(jwt.verify(inputRefreshToken)).willReturn(claims);
            given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
            given(refreshTokenRepository.findByUserId(user.getId())).willReturn(Optional.of(refreshTokenEntity));

            // When Then
            assertThatThrownBy(() -> refreshTokenService.reissueAccessToken(
                accessToken, inputRefreshToken))
                .isInstanceOf(IllegalTokenException.class);
        }

    }

    @DisplayName("reissueAccessToken 메서드 : ")
    @Nested
    class IssueRefreshToken {
        String newRefreshToken = "newRefreshToken";
        String email = "user@gmail.com";
        String[] roles = {"USER"};
        String password = "$2a$10$VgKG4LYAucDmh0PoSxjD6OaW8ADf7VUMf5ysPsQr5vh1QoVI7yXu6";
        User user = User.builder()
            .id(1L)
            .email(email)
            .password(password)
            .build();
        RefreshToken refreshTokenEntity = RefreshToken.builder()
            .id(1L)
            .token(newRefreshToken)
            .user(user)
            .build();

        @Test
        @DisplayName("RefreshToken을 생성 할 수 있다.")
        void ISSUE_REFRESH_TOKEN() {
            // Given
            Claims claims = Claims.from(email, roles);
            given(jwt.createRefreshToken(claims)).willReturn(newRefreshToken);
            given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
            given(refreshTokenRepository.findByUserId(user.getId())).willReturn(Optional.empty());

            // When
            String refreshToken = refreshTokenService.issueRefreshToken(claims);

            // Then
            assertThat(refreshToken).isEqualTo(newRefreshToken);
        }
        
        @Test
        @DisplayName("이미 RefreshToken이 있다면 새로 만들어진 RefreshToken으로 갱신해준다.")
        void UPDATE_REFRESH_TOKEN() {
            // Given
            RefreshToken PostRefreshToken = RefreshToken.builder()
                .id(1L)
                .token("PostRefreshToken")
                .user(user)
                .build();
            Claims claims = Claims.from(email, roles);
            given(jwt.createRefreshToken(claims)).willReturn(newRefreshToken);
            given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
            given(refreshTokenRepository.findByUserId(user.getId())).willReturn(Optional.of(PostRefreshToken));

            // When
            String refreshToken = refreshTokenService.issueRefreshToken(claims);

            // Then
            assertThat(refreshToken).isEqualTo(newRefreshToken);
        }
    }

}
