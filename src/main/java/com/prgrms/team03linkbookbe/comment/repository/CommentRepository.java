package com.prgrms.team03linkbookbe.comment.repository;

import com.prgrms.team03linkbookbe.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
