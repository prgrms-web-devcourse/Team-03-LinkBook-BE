package com.prgrms.team03linkbookbe.bookmark.service;


import com.prgrms.team03linkbookbe.bookmark.dto.BookmarkRequest;
import com.prgrms.team03linkbookbe.bookmark.entity.Bookmark;
import com.prgrms.team03linkbookbe.bookmark.repository.BookmarkRepository;
import com.prgrms.team03linkbookbe.common.exception.NoDataException;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import java.awt.print.Book;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookmarkService {

    BookmarkRepository bookmarkRepository;
    FolderRepository folderRepository;

    public BookmarkService(
        BookmarkRepository bookmarkRepository,
        FolderRepository folderRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.folderRepository = folderRepository;
    }

    // 북마크 생성
    public void create(BookmarkRequest dto) {
        Folder folder = folderRepository.findById(dto.getFolderId())
            .orElseThrow(NoDataException::new);

        Bookmark bookmark = dto.toEntity(folder);
        bookmarkRepository.save(bookmark);

    }

    // 북마크 수정
    public void update(Long userId, Long bookmarkId, BookmarkRequest dto) {
        Folder folder = folderRepository.findById(dto.getFolderId())
            .orElseThrow(NoDataException::new);

        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
            .orElseThrow(NoDataException::new);

        if (!bookmark.getFolder().getUser().getId().equals(userId)) {
            throw new AccessDeniedException("자신의 북마크만 수정가능합니다");
        }

        bookmark.modifyBookmark(dto, folder);
    }

    // 북마크 삭제
    public void delete(Long userId, Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
            .orElseThrow(NoDataException::new);

        if (!bookmark.getFolder().getUser().getId().equals(userId)) {
            throw new AccessDeniedException("자신의 북마크만 삭제가능합니다");
        }

        bookmarkRepository.delete(bookmark);

    }
}
