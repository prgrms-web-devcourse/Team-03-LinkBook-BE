package com.prgrms.team03linkbookbe.folder.repository;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    @Query(value = "SELECT DISTINCT f FROM Folder f LEFT JOIN FETCH f.folderTags JOIN FETCH f.user WHERE f.user = :user and f.isPrivate = :isPrivate",
        countQuery = "SELECT count(f) FROM Folder f")
    Page<Folder> findAllByUser(User user, Boolean isPrivate, Pageable pageable);

    @Query(value = "SELECT DISTINCT f FROM Folder f LEFT JOIN FETCH f.folderTags JOIN FETCH f.user WHERE f.isPrivate = :isPrivate",
        countQuery = "SELECT count(f) FROM Folder f")
    Page<Folder> findAll(Boolean isPrivate, Pageable pageable);

    @Query("SELECT DISTINCT f FROM Folder f LEFT JOIN f.bookmarks LEFT JOIN f.folderTags JOIN  f.user WHERE f.id = :folderId")
    List<Folder> findByIdWithFetchJoin(Long folderId);

    @Query(value = "SELECT f FROM Folder f JOIN FETCH f.user JOIN FETCH f.folderTags ft JOIN ft.tag t JOIN t.rootTag rt ON rt.name = :tag WHERE f.isPrivate = false",
        countQuery = "SELECT count(f) FROM Folder f")
    Page<Folder> findByRootTag(String rootTag, Pageable pageable);

    @Query(value = "SELECT f FROM Folder f JOIN FETCH f.user JOIN FETCH f.folderTags ft JOIN ft.tag t ON t.name = :tag WHERE f.isPrivate = false",
        countQuery = "SELECT count(f) FROM Folder f")
    Page<Folder> findByTag(String tag, Pageable pageable);
}
