package com.prgrms.team03linkbookbe.bookmark.service;


import com.prgrms.team03linkbookbe.bookmark.dto.BookmarkRequest;
import com.prgrms.team03linkbookbe.bookmark.entity.Bookmark;
import com.prgrms.team03linkbookbe.bookmark.repository.BookmarkRepository;
import com.prgrms.team03linkbookbe.common.exception.NoDataException;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookMarkRepository;
    private final FolderRepository folderRepository;


    // 북마크 생성
    public void create(BookmarkRequest dto) {
        // 소속 폴더가 없다면
        if(dto.getFolderId()==null){
            throw new IllegalArgumentException("폴더 id값을 입력하세요");
        }
        Folder folder = folderRepository.findById(dto.getFolderId())
            .orElseThrow(NoDataException::new);

        Bookmark bookmark = dto.toEntity(folder);
        bookMarkRepository.save(bookmark);

    }


    // 북마크 수정
    public void update(String email, Long bookmarkId, BookmarkRequest dto) {
        Folder folder = folderRepository.findById(dto.getFolderId())
            .orElseThrow(NoDataException::new);

        Bookmark bookmark = bookMarkRepository.findById(bookmarkId)
            .orElseThrow(NoDataException::new);

        if (!bookmark.getFolder().getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("자신의 북마크만 수정가능합니다");
        }

        bookmark.modifyBookmark(dto, folder);
        log.info(bookmark.toString());

    }

    // 북마크 삭제
    public void delete(String email, Long bookmarkId) {
        Bookmark bookmark = bookMarkRepository.findById(bookmarkId)
            .orElseThrow(NoDataException::new);

        if (!bookmark.getFolder().getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("자신의 북마크만 삭제가능합니다");
        }

        bookMarkRepository.delete(bookmark);

    }


}
