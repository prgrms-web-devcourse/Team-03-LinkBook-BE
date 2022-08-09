package com.prgrms.team03linkbookbe.folderTag.service;

import com.prgrms.team03linkbookbe.common.exception.NoDataException;
import com.prgrms.team03linkbookbe.folder.dto.CreateFolderRequest;
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


@Slf4j
@Service
@RequiredArgsConstructor
public class FolderTagService {

    private final TagRepository tagRepository;
    private final FolderTagRepository folderTagRepository;


    // 태그수정
    public void addFolderTag(CreateFolderRequest createFolderRequest, Folder folder) {
        List<FolderTag> original = folder.getFolderTags();

        // 유효성검사
        for(TagCategory newTag : createFolderRequest.getTags()) {
            if(Arrays.stream(TagCategory.values()).noneMatch(tagCategory -> tagCategory.getViewName() == newTag.getViewName())){
                throw new IllegalArgumentException("존재하지 않는 태그를 입력했습니다 : " + newTag.getViewName());
            }
        }

        // 없는 태그 추가
        for (TagCategory newTag : createFolderRequest.getTags()) {
            Tag tag = tagRepository.findByName(newTag).orElseThrow(NoDataException::new);

            // 없는 태그만 추가
            if (original.stream().noneMatch(folderTag -> folderTag.getTag().getName() == newTag)) {
                FolderTag folderTag = FolderTag.builder()
                    .tag(tag)
                    .folder(folder)
                    .build();

                folderTagRepository.save(folderTag);
            }
        }

        // 없애야하는 태그 제거
        for(FolderTag oldFolderTag : original){
            if(createFolderRequest.getTags().stream().noneMatch(tagCategory -> tagCategory == oldFolderTag.getTag().getName())){
                folderTagRepository.delete(oldFolderTag);
            }
        }
    }

}
