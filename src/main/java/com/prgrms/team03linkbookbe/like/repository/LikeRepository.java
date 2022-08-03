package com.prgrms.team03linkbookbe.like.repository;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.like.entity.Like;
import com.prgrms.team03linkbookbe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Integer countByFolderEquals(Folder folder);

    Optional<Like> findByIdAndUser(Long id, User user);

    List<Like> findAllByUser(User user);
}
