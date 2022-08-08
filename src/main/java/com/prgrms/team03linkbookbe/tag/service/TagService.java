package com.prgrms.team03linkbookbe.tag.service;

import com.prgrms.team03linkbookbe.rootTag.entity.RootTag;
import com.prgrms.team03linkbookbe.rootTag.entity.RootTagCategory;
import com.prgrms.team03linkbookbe.rootTag.repository.RootTagRepository;
import com.prgrms.team03linkbookbe.tag.dto.TagResponse;
import com.prgrms.team03linkbookbe.tag.entity.Tag;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import com.prgrms.team03linkbookbe.tag.repository.TagRepository;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.Root;
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
    private final RootTagRepository rootTagRepository;


    @Transactional
    public void add(){
        for(RootTagCategory root : RootTagCategory.values()) {
            RootTag parent = rootTagRepository.findByName(root).orElse(null);

            // 루트태그가 없다면
            if(parent == null){
                RootTag nRoot = RootTag.builder()
                    .name(root)
                    .build();

                parent = rootTagRepository.save(nRoot);
            }

            // 루트태그에 자식태그 매핑
            for (TagCategory sub : root.getTags()) {
                Optional<Tag> tag = tagRepository.findByName(sub);
                if (tag.isEmpty()) { // 태그가 데이터베이스에 없으면
                    Tag nTag = Tag.builder()
                        .name(sub)
                        .rootTag(parent)
                        .build();
                    tagRepository.save(nTag);
                }
            }
        }

    }

}
