package com.prgrms.team03linkbookbe.unit.refreshtoken.repository;

import static org.assertj.core.api.Assertions.*;

import com.prgrms.team03linkbookbe.refreshtoken.entity.RefreshToken;
import com.prgrms.team03linkbookbe.refreshtoken.repository.RefreshTokenRepository;
import com.prgrms.team03linkbookbe.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@Slf4j
@DataJpaTest
public class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("RefreshToken을 저장, 조회할 수 있다.")
    void REFRESHTOKEN_SAVE_TEST() {
        // Given
        String token = "token";
        User user = User.builder()
            .email("user@gmail.com")
            .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
            .name("username")
            .image("https:/")
            .role("ROLE_USER")
            .build();
        RefreshToken refreshToken = RefreshToken.builder()
            .token(token)
            .user(user)
            .build();
        testEntityManager.persist(user);


        // When
        refreshTokenRepository.save(refreshToken);
        testEntityManager.clear();
        RefreshToken findRefreshToken = refreshTokenRepository.findById(refreshToken.getId())
            .orElseThrow(() -> new IllegalArgumentException());

        // Then
        assertThat(findRefreshToken.getToken()).isEqualTo(token);
    }

    @Test
    @DisplayName("사용자의 Id로 RefreshToken을 조회할 수 있다.")
    void FIND_REFRESH_BY_USER_ID() {
        // Given
        String token = "token";
        User user = User.builder()
            .email("user@gmail.com")
            .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
            .name("username")
            .image("https:/")
            .role("ROLE_USER")
            .build();
        RefreshToken refreshToken = RefreshToken.builder()
            .token(token)
            .user(user)
            .build();
        testEntityManager.persist(user);
        testEntityManager.persist(refreshToken);

        // When
        RefreshToken findRefreshToken = refreshTokenRepository.findByUserId(user.getId())
            .orElseThrow(() -> new IllegalArgumentException());

        // Then
        assertThat(findRefreshToken.getToken()).isEqualTo(token);
    }
}
