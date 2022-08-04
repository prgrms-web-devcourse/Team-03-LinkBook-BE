package com.prgrms.team03linkbookbe.tag.service;

import com.prgrms.team03linkbookbe.tag.dto.TagResponse;
import com.prgrms.team03linkbookbe.tag.entity.Tag;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import com.prgrms.team03linkbookbe.tag.repository.TagRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final TagRepository tagRepository;


    @Transactional
    public TagResponse manage(){
        for(TagCategory tagCategory : TagCategory.values()){
            log.info("====Service=====");
            Optional<Tag> tag = tagRepository.findByName(tagCategory);
            if(tag.isEmpty()){ // 태그가 데이터베이스에 없으면
                Tag nTag = Tag.builder()
                    .name(tagCategory)
                    .build();
                tagRepository.save(nTag);
            }
        }

        List<Tag> all = tagRepository.findAll();

        return TagResponse.fromEntity(all);
    }


}
