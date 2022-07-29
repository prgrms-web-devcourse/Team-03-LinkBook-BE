package com.prgrms.team03linkbookbe.folder.service;

import com.prgrms.team03linkbookbe.common.exception.NoDataException;
import com.prgrms.team03linkbookbe.folder.dto.CreateFolderRequest;
import com.prgrms.team03linkbookbe.folder.dto.FolderIdResponse;
import com.prgrms.team03linkbookbe.folder.dto.FolderListResponse;
import com.prgrms.team03linkbookbe.folder.dto.FolderDetailResponse;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import com.prgrms.team03linkbookbe.folderTag.entity.FolderTag;
import com.prgrms.team03linkbookbe.folderTag.repository.FolderTagRepository;
import com.prgrms.team03linkbookbe.like.repository.LikeRepository;
import com.prgrms.team03linkbookbe.tag.entity.Tag;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import com.prgrms.team03linkbookbe.tag.repository.TagRepository;
import com.prgrms.team03linkbookbe.user.entity.User;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FolderService {

    private FolderRepository folderRepository;
    private FolderTagRepository folderTagRepository;
    private LikeRepository likeRepository;
    private TagRepository tagRepository;

    public FolderService(
        FolderRepository folderRepository,
        FolderTagRepository folderTagRepository,
        LikeRepository likeRepository,
        TagRepository tagRepository) {
        this.folderRepository = folderRepository;
        this.folderTagRepository = folderTagRepository;
        this.likeRepository = likeRepository;
        this.tagRepository = tagRepository;
    }

    // 폴더생성
    @Transactional(readOnly = false)
    public FolderIdResponse create(User user, CreateFolderRequest createFolderRequest) {
        createFolderRequest.setUser(user);
        Folder folder = createFolderRequest.toEntity();
        Folder save = folderRepository.save(folder);

        // 태그관계이어주기
        addFolderTag(createFolderRequest, save);

        return FolderIdResponse.fromEntity(folder.getId());
    }



    // 특정 폴더조회
    public FolderDetailResponse detail(Long folderId) {
        Folder folder = folderRepository.findById(folderId).orElseThrow(NoDataException::new);
        FolderDetailResponse folderDetailResponse = FolderDetailResponse
            .fromEntity(folder);

        // 좋아요 개수 넣기
        folderDetailResponse.setLikes(likeRepository.countByFolderEquals(folder));
        return folderDetailResponse;
    }


    // 특정 사용자의 폴더전체조회
    public FolderListResponse getAllByUser(User user) {
        List<Folder> folders = folderRepository.findAllByUser(user);
        return FolderListResponse.fromEntity(user, folders);
    }


    // 특정 폴더 수정
    @Transactional(readOnly = false)
    public FolderIdResponse update(Long folderId, CreateFolderRequest createFolderRequest) {
        Folder folder = createFolderRequest.toEntity();
        Folder save = folderRepository.save(folder); // merge

        // 태그관계끊어주기
        folderTagRepository.deleteAllByFolder(save);

        // 태그관계이어주기
        addFolderTag(createFolderRequest, save);

        return FolderIdResponse.fromEntity(save.getId());
    }

    // 특정 폴더 삭제
    @Transactional(readOnly = false)
    public void delete(Long folderId) {
        Folder folder = folderRepository.findById(folderId).orElseThrow(NoDataException::new);
        folderRepository.delete(folder);
    }

    // 태그추가
    private void addFolderTag(CreateFolderRequest createFolderRequest, Folder folder) {
        for (TagCategory tagCategory : createFolderRequest.getTags()) {
            Tag tag = tagRepository.findByName(tagCategory.name())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 태그를 입력했습니다."));

            FolderTag folderTag = FolderTag.builder()
                .tag(tag)
                .folder(folder)
                .build();

            folderTagRepository.save(folderTag);
        }
    }


}
