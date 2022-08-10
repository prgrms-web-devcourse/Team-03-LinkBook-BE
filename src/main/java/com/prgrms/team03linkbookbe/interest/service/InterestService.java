package com.prgrms.team03linkbookbe.interest.service;

import com.prgrms.team03linkbookbe.common.exception.NoDataException;
import com.prgrms.team03linkbookbe.folderTag.entity.FolderTag;
import com.prgrms.team03linkbookbe.interest.entity.Interest;
import com.prgrms.team03linkbookbe.interest.entity.SubTag;
import com.prgrms.team03linkbookbe.interest.repository.InterestRepository;
import com.prgrms.team03linkbookbe.tag.entity.Tag;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import com.prgrms.team03linkbookbe.tag.repository.TagRepository;
import com.prgrms.team03linkbookbe.user.entity.User;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterestService {

    private final TagRepository tagRepository;
    private final InterestRepository interestRepository;

    @Transactional
    public void updateInterests(List<String> subTagName, User user) {
        List<Interest> original = user.getInterests();

        List<SubTag> subTags = subTagName.stream().map(subtag -> SubTag.ENUM_MAP.get(subtag))
            .collect(Collectors.toList());

        subTags.stream()
            .filter(subTag -> original.stream().noneMatch(origin -> origin.getTag().name().equals(subTag.name())))
            .forEach(subTag -> {
                Interest newInterest = Interest.builder()
                    .tag(subTag)
                    .user(user)
                    .build();
                original.add(newInterest);
                interestRepository.save(newInterest);
            });

        original.stream()
        .filter(origin -> subTags.stream().noneMatch(subTag -> subTag.name().equals(origin.getTag().name())))
            .collect(Collectors.toList())
            .forEach(origin -> {
                interestRepository.delete(origin);
                original.remove(origin);
            });

    }

}
