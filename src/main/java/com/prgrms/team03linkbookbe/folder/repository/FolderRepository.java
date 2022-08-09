package com.prgrms.team03linkbookbe.folder.repository;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.rootTag.entity.RootTag;
import com.prgrms.team03linkbookbe.rootTag.entity.RootTagCategory;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import com.prgrms.team03linkbookbe.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    @Query(value = "SELECT DISTINCT f FROM Folder f LEFT JOIN FETCH f.folderTags WHERE f.user = :user and f.isPrivate = :isPrivate",
            countQuery = "SELECT count(f) FROM Folder f")
    Page<Folder> findAllByUser(User user, Boolean isPrivate, Pageable pageable);

    @Query(value = "SELECT DISTINCT f FROM Folder f JOIN FETCH f.user LEFT JOIN FETCH f.folderTags WHERE f.isPrivate = :isPrivate",
            countQuery = "SELECT count(f) FROM Folder f")
    Page<Folder> findAll(Boolean isPrivate, Pageable pageable);

    //TODO : 필터링부분구현
//    @Query(value = "SELECT DISTINCT f FROM Folder f JOIN FETCH f.folderTags ft WHERE ft.tag.name = :tag")
//    Page<Folder> findAllByTag(TagCategory tag);
//
//    @Query(value = "SELECT DISTINCT f FROM Folder f JOIN FETCH f.folderTags ft WHERE ft.tag.rootTag.name = :root")
//    Page<Folder> findAllByRootTag(RootTag root);


    @Query("SELECT DISTINCT f FROM Folder f LEFT JOIN f.bookmarks LEFT JOIN f.folderTags JOIN f.user WHERE f.id = :folderId")
    List<Folder> findByIdWithFetchJoin(Long folderId);

    @Query(value = "SELECT f FROM Folder f JOIN FETCH f.user JOIN FETCH f.folderTags ft JOIN ft.tag t JOIN t.rootTag rt ON rt.name = :rootTag WHERE f.isPrivate = false",
        countQuery = "SELECT count(f) FROM Folder f")
    Page<Folder> findByRootTag(RootTagCategory rootTag, Pageable pageable);

    @Query(value = "SELECT f FROM Folder f JOIN FETCH f.user JOIN FETCH f.folderTags ft JOIN ft.tag t ON t.name = :tag WHERE f.isPrivate = false",
        countQuery = "SELECT count(f) FROM Folder f")
    Page<Folder> findByTag(TagCategory tag, Pageable pageable);
}
