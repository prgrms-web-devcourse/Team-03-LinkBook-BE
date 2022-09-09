package com.prgrms.team03linkbookbe.folder.service;

import com.prgrms.team03linkbookbe.bookmark.dto.BookmarkRequest;
import com.prgrms.team03linkbookbe.bookmark.entity.Bookmark;
import com.prgrms.team03linkbookbe.bookmark.repository.BookmarkRepository;
import com.prgrms.team03linkbookbe.common.exception.NoDataException;
import com.prgrms.team03linkbookbe.folder.dto.CreateFolderRequest;
import com.prgrms.team03linkbookbe.folder.dto.FolderDetailResponse;
import com.prgrms.team03linkbookbe.folder.dto.FolderIdResponse;
import com.prgrms.team03linkbookbe.folder.dto.FolderListByUserResponse;
import com.prgrms.team03linkbookbe.folder.dto.FolderListResponse;
import com.prgrms.team03linkbookbe.folder.dto.OriginFolderResponse;
import com.prgrms.team03linkbookbe.folder.dto.PinnedListResponse;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.exception.IllegalAccessToPrivateFolderException;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import com.prgrms.team03linkbookbe.folderTag.service.FolderTagService;
import com.prgrms.team03linkbookbe.jwt.JwtAuthentication;
import com.prgrms.team03linkbookbe.like.entity.Like;
import com.prgrms.team03linkbookbe.like.repository.LikeRepository;
import com.prgrms.team03linkbookbe.rootTag.entity.RootTagCategory;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import java.util.ArrayList;
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
        List<Like> likes;
        Page<Folder> all;

        if (auth != null) {
            User user = userRepository.findByEmail(auth.email)
                .orElseThrow(NoDataException::new);
            likes = likeRepository.findAllByUser(user);
            all = folderRepository.findAll(false, pageable, user);
        } else {
            likes = new ArrayList<>();
            all = folderRepository.findAll(false, pageable);
        }

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
                .orElseThrow(NoDataException::new);
            likes = likeRepository.findAllByUser(user);
        }

        if (folder.get(0).getOriginId() != null) {
            OriginFolderResponse originFolder = OriginFolderResponse.fromEntity(
                folderRepository.findById(folder.get(0).getOriginId())
                    .orElseThrow(NoDataException::new));

            return FolderDetailResponse
                .fromEntity(folder.get(0),
                    likes.stream().anyMatch(l -> l.getFolder().getId().equals(folderId)),
                    originFolder);
        }

        return FolderDetailResponse
            .fromEntity(folder.get(0),
                likes.stream().anyMatch(l -> l.getFolder().getId().equals(folderId)));
    }

    // 특정 사용자의 폴더전체조회
    public FolderListByUserResponse getAllByUser(Long userId, String email, String isPrivate,
        Pageable pageable) {

        Page<Folder> folders;
        User user = userRepository.findById(userId).orElseThrow(NoDataException::new);
        List<Like> likes = likeRepository.findAllByUser(user);

        if ("true".equals(isPrivate)) {
            if (email == null) { // 토큰이 없는 경우
                throw new IllegalAccessToPrivateFolderException();
            } else if (!email.equals(user.getEmail())) { // 본인이 아닌경우
                throw new IllegalAccessToPrivateFolderException();
            } else {
                folders = folderRepository.findAllByUser(user, true, pageable);
            }
        } else if ("false".equals(isPrivate)) {
            folders = folderRepository.findAllByUser(user, false, pageable);
        } else {
            folders = folderRepository.findAllByUser(user, pageable);
        }

        return FolderListByUserResponse.fromEntity(user, folders, likes);
    }

    public PinnedListResponse getAllPinned(String email) {
        List<Folder> folders = folderRepository.findAllPinned(email, true);
        return PinnedListResponse.fromEntity(folders);
    }

    // 특정 문자열을 제목에 포함한 폴더전체 조회
    public FolderListResponse getAllByTitle(Pageable pageable, String title,
        JwtAuthentication auth) {
        List<Like> likes;
        Page<Folder> all;

        if (auth != null) {
            User user = userRepository.findByEmail(auth.email)
                .orElseThrow(NoDataException::new);
            likes = likeRepository.findAllByUser(user);
            all = folderRepository.findAllByTitle(false, pageable, title, user);
        } else {
            likes = new ArrayList<>();
            all = folderRepository.findAllByTitle(false, pageable, title);
        }

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


        // 1. 기존 북마크 전체 가져와서 지우거나 수정하거나
        List<Bookmark> oldBookmark = bookmarkRepository.findAllByFolder(folder);
        for(Bookmark old : oldBookmark){
            log.info("{}",old.getId());
            boolean remove = true;
            for(BookmarkRequest bookmarkRequest : createFolderRequest.getBookmarks()){
                if(old.getId() == bookmarkRequest.getId()) { // 존재하는 경우 내용 수정
                    log.info("{} {}",old.getId(), bookmarkRequest.getId());
                    remove = false;
                    old.modifyBookmark(bookmarkRequest,folder);
                    break;
                }
            }
            log.info("{}",remove);
            if(remove){
                bookmarkRepository.delete(old);
            }
        }


        // 2. id 없는 것은 새로 생성해주기
        for(BookmarkRequest bookmarkRequest : createFolderRequest.getBookmarks()) {
            if(bookmarkRequest.getId() == null) {
                Bookmark bookmark = bookmarkRequest.toEntity(folder);
                bookmarkRepository.save(bookmark);
            }
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

    public FolderListResponse getByRootTag(String rootTag, Pageable pageable,
        JwtAuthentication auth) {
        Page<Folder> folders;

        RootTagCategory rootTagCategory = RootTagCategory.from(rootTag);

        if (rootTagCategory == RootTagCategory.ALL) {
            folders = folderRepository.findAll(false, pageable);
        } else {
            folders = folderRepository.findByRootTag(rootTagCategory, pageable);
        }

        List<Like> likes = new ArrayList<>();

        if (auth != null) {
            User user = userRepository.findByEmail(auth.email)
                .orElseThrow(NoDataException::new);
            likes = likeRepository.findAllByUser(user);
        }

        return FolderListResponse.fromEntity(folders, likes);
    }

    public FolderListResponse getByTag(String tag, Pageable pageable,
        JwtAuthentication auth) {
        TagCategory tagName = TagCategory.from(tag);
        Page<Folder> folders = folderRepository.findByTag(tagName, pageable);
        List<Like> likes;

        if (auth != null) {
            User user = userRepository.findByEmail(auth.email)
                .orElseThrow(NoDataException::new);
            likes = likeRepository.findAllByUser(user);
        } else {
            likes = new ArrayList<>();
        }

        return FolderListResponse.fromEntity(folders, likes);
    }
}
