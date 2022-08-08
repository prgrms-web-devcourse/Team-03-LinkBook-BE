package com.prgrms.team03linkbookbe.folder.service;

import com.prgrms.team03linkbookbe.common.exception.NoDataException;
import com.prgrms.team03linkbookbe.folder.dto.CreateFolderRequest;
import com.prgrms.team03linkbookbe.folder.dto.FolderDetailResponse;
import com.prgrms.team03linkbookbe.folder.dto.FolderIdResponse;
import com.prgrms.team03linkbookbe.folder.dto.FolderListByUserResponse;
import com.prgrms.team03linkbookbe.folder.dto.FolderListResponse;
import com.prgrms.team03linkbookbe.folder.dto.TagRequest;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import com.prgrms.team03linkbookbe.folderTag.entity.FolderTag;
import com.prgrms.team03linkbookbe.folderTag.repository.FolderTagRepository;
import com.prgrms.team03linkbookbe.jwt.JwtAuthentication;
import com.prgrms.team03linkbookbe.like.repository.LikeRepository;
import com.prgrms.team03linkbookbe.tag.entity.Tag;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import com.prgrms.team03linkbookbe.tag.repository.TagRepository;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FolderService {

    private final FolderRepository folderRepository;
    private final FolderTagRepository folderTagRepository;
    private final LikeRepository likeRepository;
    private final TagRepository tagRepository;
    private final RootTagRepository rootTagRepository;
    private final UserRepository userRepository;


    // 폴더생성
    @Transactional
    public FolderIdResponse create(JwtAuthentication auth,
        CreateFolderRequest createFolderRequest) {
        User user = userRepository.findByEmail(auth.email).orElseThrow(NoDataException::new);
        createFolderRequest.setUser(user);
        Folder folder = createFolderRequest.toEntity();
        Folder save = folderRepository.save(folder);

        // 태그관계이어주기
        addFolderTag(createFolderRequest, save);

        return FolderIdResponse.fromEntity(folder.getId());
    }

    // 전체폴더조회
    public FolderListResponse getAll(Pageable pageable) {
        Page<Folder> all = folderRepository.findAll(false, pageable);
        return FolderListResponse.fromEntity(all);
    }


    // 특정 폴더조회
    public FolderDetailResponse detail(Long folderId) {
        List<Folder> folder = folderRepository.findByIdWithFetchJoin(folderId);
        if (folder.size() != 1) {
            throw new NoDataException();
        }

        return FolderDetailResponse
            .fromEntity(folder.get(0));
    }


    // 특정 사용자의 폴더전체조회
    public FolderListByUserResponse getAllByUser(Long userId, Boolean isPrivate,
        Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(NoDataException::new);
        Page<Folder> folders = folderRepository.findAllByUser(user, isPrivate, pageable);
        return FolderListByUserResponse.fromEntity(user, folders);
    }


    // 특정 폴더 수정
    @Transactional
    public FolderIdResponse update(String email, Long folderId,
        CreateFolderRequest createFolderRequest) {
        Folder folder = folderRepository.findById(folderId).orElseThrow(NoDataException::new);
        if (!folder.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("자신의 폴더만 수정가능합니다");
        }

        // dirty check로 update
        folder.modifyFolder(createFolderRequest);

        // 태그관계끊어주기
        folderTagRepository.deleteAllByFolder(folder);

        // 태그관계이어주기
        addFolderTag(createFolderRequest, folder);

        return FolderIdResponse.fromEntity(folder.getId());
    }

    // 특정 폴더 삭제
    @Transactional
    public void delete(String email, Long folderId) {
        Folder folder = folderRepository.findById(folderId).orElseThrow(NoDataException::new);
        if (!folder.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("자신의 폴더만 삭제가능합니다");
        }

        folderRepository.delete(folder);
    }

    public FolderListResponse getByTag(TagRequest tagRequest, Pageable pageable) {
        String tagName = tagRequest.getTag();
        Page<Folder> folders;
        if (rootTagRepository.existsByName(tagName)) {
            folders = folderRepository.findByRootTag(tagName, pageable);
        } else if (tagRepository.existsByName(tagName)) {
            folders = folderRepository.findByTag(tagName, pageable);
        } else {
            throw new NoDataException();
        }

        return FolderListResponse.fromEntity(folders);
    }

    // 태그추가
    private void addFolderTag(CreateFolderRequest createFolderRequest, Folder folder) {
        for (TagCategory tagCategory : createFolderRequest.getTags()) {
            Tag tag = tagRepository.findByName(tagCategory)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 태그를 입력했습니다."));

            FolderTag folderTag = FolderTag.builder()
                .tag(tag)
                .folder(folder)
                .build();

            folderTagRepository.save(folderTag);
        }
    }

}
