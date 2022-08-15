package com.prgrms.team03linkbookbe.unit.interest.repository;

import static org.assertj.core.api.Assertions.*;

import com.prgrms.team03linkbookbe.common.exception.NoDataException;
import com.prgrms.team03linkbookbe.interest.entity.Interest;
import com.prgrms.team03linkbookbe.interest.entity.SubTag;
import com.prgrms.team03linkbookbe.interest.repository.InterestRepository;
import com.prgrms.team03linkbookbe.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@Slf4j
@DataJpaTest
public class InterestRepositoryTest {

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Interest 저장 조회 할 수 있다.")
    void SAVE_INTEREST_TEST() {
        // Given
        User user = User.builder()
            .email("user@gmail.com")
            .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
            .name("username")
            .introduce("introduce")
            .image("https:/")
            .role("ROLE_USER")
            .build();

        Interest interest = Interest.builder()
            .tag(SubTag.CHINESE_FOOD)
            .user(user)
            .build();

        em.persist(user);
        interestRepository.save(interest);
        em.clear();

        // When
        Interest findInterest = interestRepository.findById(interest.getId())
            .orElseThrow(() -> new NoDataException());

        // Then
        assertThat(findInterest).isNotNull();
    }

}
