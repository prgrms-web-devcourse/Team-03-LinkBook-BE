package com.prgrms.team03linkbookbe.unit.interest.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doNothing;

import com.prgrms.team03linkbookbe.interest.entity.Interest;
import com.prgrms.team03linkbookbe.interest.entity.SubTag;
import com.prgrms.team03linkbookbe.interest.repository.InterestRepository;
import com.prgrms.team03linkbookbe.interest.service.InterestService;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import com.prgrms.team03linkbookbe.user.entity.User;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class InterestServiceTest {

    @InjectMocks
    InterestService interestService;

    @Mock
    InterestRepository interestRepository;

    @Test
    @DisplayName("관심 태그를 업데이트 할 수 있다.")
    void UPDATE_INTERESTS_TEST() {
        // Given
        User user = User.builder()
            .id(1L)
            .name("유저1")
            .build();

        Interest interest1 = Interest.builder()
            .id(1L)
            .tag(SubTag.DOG)
            .user(user)
            .build();

        Interest interest2 = Interest.builder()
            .id(2L)
            .tag(SubTag.CAT)
            .user(user)
            .build();

        user.addInterest(interest1);
        user.addInterest(interest2);

        List<String> subTag = List.of("개", "고양이");

//        given(interestRepository.save(any())).willReturn(Interest.builder().build());
//        willDoNothing().given(interestRepository).delete(any());

        // When
        interestService.updateInterests(subTag, user);

        // Then
        List<String> subTags = user.getInterests().stream()
            .map(interest -> interest.getTag().getViewName()).collect(Collectors.toList());
        Assertions.assertThat(subTags).containsOnly("개", "고양이");
    }
}
