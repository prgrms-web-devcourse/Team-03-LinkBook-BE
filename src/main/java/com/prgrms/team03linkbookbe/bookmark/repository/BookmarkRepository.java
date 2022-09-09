package com.prgrms.team03linkbookbe.bookmark.repository;

import com.prgrms.team03linkbookbe.bookmark.entity.Bookmark;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import java.awt.print.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query("select b from Bookmark b where b.folder = :folder")
    List<Bookmark> findAllByFolder(Folder folder);
}
