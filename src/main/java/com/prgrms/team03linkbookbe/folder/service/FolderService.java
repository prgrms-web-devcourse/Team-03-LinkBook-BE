package com.prgrms.team03linkbookbe.folder.service;

import com.prgrms.team03linkbookbe.bookmark.dto.BookmarkRequest;
import com.prgrms.team03linkbookbe.bookmark.entity.Bookmark;
import com.prgrms.team03linkbookbe.bookmark.repository.BookmarkRepository;
import com.prgrms.team03linkbookbe.common.exception.NoDataException;
import com.prgrms.team03linkbookbe.folder.dto.*;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import com.prgrms.team03linkbookbe.folderTag.service.FolderTagService;
import com.prgrms.team03linkbookbe.jwt.JwtAuthentication;
import com.prgrms.team03linkbookbe.like.entity.Like;
import com.prgrms.team03linkbookbe.like.repository.LikeRepository;
import com.prgrms.team03linkbookbe.rootTag.entity.RootTagCategory;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FolderService {

    private final FolderRepository folderRepository;
    private final FolderTagService folderTagService;
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;
    private final LikeRepository likeRepository;

    // 폴더생성
    @Transactional
    public FolderIdResponse create(JwtAuthentication auth,
                                   CreateFolderRequest createFolderRequest) {
        User user = userRepository.findByEmail(auth.email).orElseThrow(NoDataException::new);

        Folder folder = createFolderRequest.toEntity(user);
        Folder save = folderRepository.save(folder);

        // 태그 관계이어주기
        folderTagService.addFolderTag(createFolderRequest, save);

        // 북마크 관계이어주기
        for (BookmarkRequest bookmarkRequest : createFolderRequest.getBookmarks()) {
            Bookmark bookmark = bookmarkRequest.toEntity(folder);
            bookmarkRepository.save(bookmark);
        }

        return FolderIdResponse.fromEntity(save.getId());
    }

    // 전체폴더조회
    public FolderListResponse getAll(Pageable pageable, JwtAuthentication auth) {
        List<Like> likes = new ArrayList<>();

        if (auth != null) {
            User user = userRepository.findByEmail(auth.email)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 정보입니다."));
            likes = likeRepository.findAllByUser(user);
        }

        Page<Folder> all = folderRepository.findAll(false, pageable);

        return FolderListResponse.fromEntity(all, likes);
    }


    // 특정 폴더조회
    public FolderDetailResponse detail(Long folderId, JwtAuthentication auth) {
        List<Folder> folder = folderRepository.findByIdWithFetchJoin(folderId);
        if (folder.size() != 1) {
            throw new NoDataException();
        }

        List<Like> likes = new ArrayList<>();

        if (auth != null) {
            User user = userRepository.findByEmail(auth.email)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 정보입니다."));
            likes = likeRepository.findAllByUser(user);
        }

        if (folder.get(0).getOriginId() != null) {
            OriginFolderResponse originFolder = OriginFolderResponse.fromEntity(folderRepository.findById(folder.get(0).getOriginId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 원본 폴더 정보입니다.")));

            return FolderDetailResponse
                    .fromEntity(folder.get(0),
                            likes.stream().anyMatch(l -> l.getFolder().getId().equals(folderId)), originFolder);
        }

        return FolderDetailResponse
                .fromEntity(folder.get(0),
                        likes.stream().anyMatch(l -> l.getFolder().getId().equals(folderId)));
    }

    // 특정 사용자의 폴더전체조회
    public FolderListByUserResponse getAllByUser(Long userId, Boolean isPrivate,
                                                 Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(NoDataException::new);
        List<Like> likes = likeRepository.findAllByUser(user);
        Page<Folder> folders = folderRepository.findAllByUser(user, isPrivate, pageable);
        return FolderListByUserResponse.fromEntity(user, folders, likes);
    }

    // 특정 문자열을 제목에 포함한 폴더전체 조회
    public FolderListResponse getAllByTitle(Pageable pageable, String title,
                                            JwtAuthentication auth) {
        List<Like> likes = new ArrayList<>();

        if (auth != null) {
            User user = userRepository.findByEmail(auth.email)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 정보입니다."));
            likes = likeRepository.findAllByUser(user);
        }

        Page<Folder> all = folderRepository.findAllByTitle(false, pageable, title);
        return FolderListResponse.fromEntity(all, likes);
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

        // 북마크 관계이어주기
//        for(BookmarkRequest bookmark : createFolderRequest.getBookmarks()){
//            if(bookmark.getId() == null){ // 생성
//                bookmarkService.create(bookmark);
//            }
//            else{ // 수정
//                bookmarkService.update(email,bookmark.getId(),bookmark);
//            }
//        }

        // 북마크 전체
        bookmarkRepository.deleteAllByFolder(folder);

        // 북마크 관계이어주기
        for (BookmarkRequest bookmarkRequest : createFolderRequest.getBookmarks()) {
            Bookmark bookmark = bookmarkRequest.toEntity(folder);
            bookmarkRepository.save(bookmark);
        }

        // 태그 관계이어주기
        folderTagService.addFolderTag(createFolderRequest, folder);

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

    public FolderListResponse getByRootTag(RootTagRequest rootTagRequest, Pageable pageable,
                                           JwtAuthentication auth) {
        RootTagCategory rootTagName = rootTagRequest.getRootTag();
        Page<Folder> folders = folderRepository.findByRootTag(rootTagName, pageable);

        List<Like> likes = new ArrayList<>();

        if (auth != null) {
            User user = userRepository.findByEmail(auth.email)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 정보입니다."));
            likes = likeRepository.findAllByUser(user);
        }

        return FolderListResponse.fromEntity(folders, likes);
    }

    public FolderListResponse getByTag(TagRequest tagRequest, Pageable pageable,
                                       JwtAuthentication auth) {
        TagCategory tagName = tagRequest.getTag();
        Page<Folder> folders = folderRepository.findByTag(tagName, pageable);

        List<Like> likes = new ArrayList<>();
        if (auth != null) {
            User user = userRepository.findByEmail(auth.email)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 정보입니다."));
            likes = likeRepository.findAllByUser(user);
        }

        return FolderListResponse.fromEntity(folders, likes);
    }
}
