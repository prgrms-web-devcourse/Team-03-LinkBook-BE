package com.prgrms.team03linkbookbe.folder.repository;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
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

    @Query(value = "SELECT DISTINCT f FROM Folder f LEFT JOIN FETCH f.folderTags WHERE f.user = :user",
        countQuery = "SELECT count(f) FROM Folder f")
    Page<Folder> findAllByUser(User user, Pageable pageable);

    @Query(value = "SELECT DISTINCT f FROM Folder f JOIN FETCH f.user LEFT JOIN FETCH f.folderTags WHERE f.isPrivate = :isPrivate OR ( f.isPrivate = true AND f.user = :user ) ",
        countQuery = "SELECT count(f) FROM Folder f WHERE f.isPrivate = :isPrivate OR ( f.isPrivate = true AND f.user = :user ) ")
    Page<Folder> findAll(Boolean isPrivate, Pageable pageable, User user);

    @Query(value = "SELECT DISTINCT f FROM Folder f JOIN FETCH f.user LEFT JOIN FETCH f.folderTags WHERE f.isPrivate = :isPrivate",
        countQuery = "SELECT count(f) FROM Folder f WHERE f.isPrivate = :isPrivate")
    Page<Folder> findAll(Boolean isPrivate, Pageable pageable);

    @Query("SELECT DISTINCT f FROM Folder f JOIN FETCH f.user LEFT JOIN FETCH f.bookmarks LEFT JOIN f.folderTags JOIN f.user u ON u.email = :email WHERE f.isPinned = :isPinned")
    List<Folder> findAllPinned(String email, Boolean isPinned);

    //TODO : 필터링부분구현
//    @Query(value = "SELECT DISTINCT f FROM Folder f JOIN FETCH f.folderTags ft WHERE ft.tag.name = :tag")
//    Page<Folder> findAllByTag(TagCategory tag);
//
//    @Query(value = "SELECT DISTINCT f FROM Folder f JOIN FETCH f.folderTags ft WHERE ft.tag.rootTag.name = :root")
//    Page<Folder> findAllByRootTag(RootTag root);


    @Query("SELECT DISTINCT f FROM Folder f LEFT JOIN f.bookmarks LEFT JOIN f.folderTags JOIN f.user WHERE f.id = :folderId")
    List<Folder> findByIdWithFetchJoin(Long folderId);

    @Query(value = "SELECT DISTINCT f FROM Folder f LEFT JOIN FETCH f.folderTags WHERE ( f.isPrivate = :isPrivate OR ( f.isPrivate = true AND f.user = :user ) ) AND f.title LIKE CONCAT('%',:title,'%')",
        countQuery = "SELECT count(f) FROM Folder f WHERE ( f.isPrivate = :isPrivate OR ( f.isPrivate = true AND f.user = :user ) ) AND f.title LIKE CONCAT('%',:title,'%')")
    Page<Folder> findAllByTitle(Boolean isPrivate, Pageable pageable, String title, User user);

    @Query(value = "SELECT DISTINCT f FROM Folder f LEFT JOIN FETCH f.folderTags WHERE f.isPrivate = :isPrivate AND f.title LIKE CONCAT('%',:title,'%')",
        countQuery = "SELECT count(f) FROM Folder f WHERE f.isPrivate = :isPrivate AND f.title LIKE CONCAT('%',:title,'%')")
    Page<Folder> findAllByTitle(Boolean isPrivate, Pageable pageable, String title);

    @Query(value = "SELECT DISTINCT f FROM Folder f " +
        "JOIN FETCH f.user JOIN FETCH f.folderTags ft " +
        "WHERE f.isPrivate = false AND f.id IN " +
        "(SELECT subf.id FROM Folder subf JOIN subf.folderTags subft JOIN subft.tag subt JOIN subt.rootTag subrt " +
        "ON subrt.name = :rootTag)",
        countQuery = "SELECT count(f) FROM Folder f")
    Page<Folder> findByRootTag(RootTagCategory rootTag, Pageable pageable);

    @Query(value = "SELECT DISTINCT f FROM Folder f " +
        "JOIN FETCH f.user JOIN FETCH f.folderTags ft " +
        "WHERE f.isPrivate = false AND f.id IN " +
        "(SELECT subf.id FROM Folder subf JOIN subf.folderTags subft JOIN subft.tag subt " +
        "ON subt.name = :tag)",
        countQuery = "SELECT count(f) FROM Folder f")
    Page<Folder> findByTag(TagCategory tag, Pageable pageable);
}
