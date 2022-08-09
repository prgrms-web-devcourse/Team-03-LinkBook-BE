package com.prgrms.team03linkbookbe.bookmark.repository;

import com.prgrms.team03linkbookbe.bookmark.entity.Bookmark;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    void deleteAllByFolder(Folder folder);
}
