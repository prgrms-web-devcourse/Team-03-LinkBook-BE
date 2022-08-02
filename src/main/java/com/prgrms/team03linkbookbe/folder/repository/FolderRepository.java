package com.prgrms.team03linkbookbe.folder.repository;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.user.entity.User;
import java.awt.print.Pageable;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FolderRepository extends JpaRepository<Folder, Long> {


    Page<Folder> findAllByUserAndIsPrivate(User user, Boolean isPrivate, Pageable pageable);

}
