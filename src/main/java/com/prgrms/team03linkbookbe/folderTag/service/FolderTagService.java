package com.prgrms.team03linkbookbe.folderTag.service;

import com.prgrms.team03linkbookbe.common.exception.NoDataException;
import com.prgrms.team03linkbookbe.folder.dto.CreateFolderRequest;
import com.prgrms.team03linkbookbe.folder.dto.TagRequest;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folderTag.entity.FolderTag;
import com.prgrms.team03linkbookbe.folderTag.repository.FolderTagRepository;
import com.prgrms.team03linkbookbe.tag.entity.Tag;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import com.prgrms.team03linkbookbe.tag.repository.TagRepository;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class FolderTagService {

    private final TagRepository tagRepository;
    private final FolderTagRepository folderTagRepository;


    // 태그수정
    @Transactional
    public void addFolderTag(CreateFolderRequest createFolderRequest, Folder folder) {
        // 기존 폴더의 태그
        List<FolderTag> original = folder.getFolderTags();

        // 없는 태그 추가
        for (TagRequest newTag : createFolderRequest.getTags()) {
            if(newTag.getId() == null){
                // 유효성 검사
                Tag tag = tagRepository.findByName(newTag.getName()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 태그를 입력했습니다 : " + newTag.getName()));

                // 관계 만들어주기
                FolderTag folderTag = FolderTag.builder()
                    .tag(tag)
                    .folder(folder)
                    .build();

                folderTagRepository.save(folderTag);

            }
        }

        // 없애야하는 태그 제거
        for(FolderTag oldFolderTag : original){
            if(createFolderRequest.getTags().stream().noneMatch(tag -> tag.getName().equals(oldFolderTag.getTag().getName()))){
                folderTagRepository.delete(oldFolderTag);
            }
        }
    }

}
