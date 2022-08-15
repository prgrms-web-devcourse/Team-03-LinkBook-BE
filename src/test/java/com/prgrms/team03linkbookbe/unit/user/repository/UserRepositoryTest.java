package com.prgrms.team03linkbookbe.unit.user.repository;

import static org.assertj.core.api.Assertions.*;

import com.prgrms.team03linkbookbe.interest.entity.Interest;
import com.prgrms.team03linkbookbe.interest.entity.SubTag;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@Slf4j
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("User를 저장할 수 있다.")
    void SAVE_USER_TEST() {
        // Given
        User user = User.builder()
            .email("user@gmail.com")
            .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
            .name("username")
            .introduce("introduce")
            .image("https:/")
            .role("ROLE_USER")
            .build();

        // When
        User saveUser = userRepository.save(user);

        // Then
        assertThat(saveUser.getId()).isEqualTo(user.getId());
    }
    
    @Test
    @DisplayName("이메일을 가진 사용자가 있는지 검색할 수 있다.")
    void FIND_USER_BY_EMAIL_TEST() {
        // Given
        User user = User.builder()
            .email("user@gmail.com")
            .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
            .introduce("introduce")
            .image("https:/")
            .build();
        userRepository.save(user);

        // When
        Optional<User> findUser = userRepository.findByEmail(user.getEmail());
        System.out.println(findUser.get().getEmail() + findUser.get().getName() + findUser.get().getRole());


        // Then
        assertThat(findUser.isPresent()).isTrue();
    }

    @Test
    @DisplayName("이메일을 가진 사용자가 존재하는지 확인할 수 있다.")
    void EXISTS_BY_EMAIL_TEST() {
        // Given
        User user = User.builder()
            .email("user@gmail.com")
            .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
            .introduce("introduce")
            .image("https:/")
            .build();
        userRepository.save(user);

        // When
        Boolean isExist = userRepository.existsByEmail(user.getEmail());

        // Then
        assertThat(isExist).isTrue();
    }

    @Test
    @DisplayName("이메일을 이용해서 사용자를 찾아서 사용자 정보를 수정할 수 있다.")
    void FIND_BY_EMAIL_FETCH_JOIN_INTEREST_TEST() {
        // Given
        User user = User.builder()
            .email("user@gmail.com")
            .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
            .introduce("introduce")
            .image("https:/")
            .build();

        Interest interest1 = Interest.builder()
            .tag(SubTag.CHINESE_FOOD)
            .build();
        Interest interest2 = Interest.builder()
            .tag(SubTag.ACTION_MOVIE)
            .build();

        user.addInterest(interest1);
        user.addInterest(interest2);
        testEntityManager.persist(user);
        testEntityManager.persist(interest1);
        testEntityManager.persist(interest2);

        // When
        User findUser = userRepository.findByEmailFetchJoinInterests(
            user.getEmail()).orElseThrow(() -> new IllegalArgumentException());

        // Then
        assertThat(findUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(findUser.getInterests().size()).isEqualTo(2);

    }
}
