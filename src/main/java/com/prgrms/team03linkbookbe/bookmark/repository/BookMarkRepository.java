package com.prgrms.team03linkbookbe.bookmark.repository;

import com.prgrms.team03linkbookbe.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookMarkRepository extends JpaRepository<Bookmark, Long> {

}
