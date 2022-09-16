package com.prgrms.team03linkbookbe.comment.repository;

import com.prgrms.team03linkbookbe.comment.entity.Comment;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndUser(Long id, User user);

    List<Comment> findAllByParentId(Long parentId);

    @Query("select c from Comment c where c.folder = :folder")
    List<Comment> findAllByFolder(Folder folder);

    @Query("select c from Comment c where c.folder = :folder and c.parent IS NULL")
    List<Comment> findAllParentsByFolder(Folder folder);
}
