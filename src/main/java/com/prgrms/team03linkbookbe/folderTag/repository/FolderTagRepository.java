package com.prgrms.team03linkbookbe.folderTag.repository;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folderTag.entity.FolderTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderTagRepository extends JpaRepository<FolderTag, Long> {

    void deleteAllByFolder(Folder folder);

    // TODO : 필터링
//    @Query("SELECT DISTINCT ft.folder FROM FolderTag ft JOIN FETCH ft.folder JOIN FETCH ft.tag WHERE ft.tag.rootTag.name = :root order by ft.folder.likes DESC ")
//    Page<FolderTag> findFolderTagByRootTagOrderByLikes(RootTagCategory root);
//
//    @Query("SELECT DISTINCT ft.folder FROM FolderTag ft JOIN FETCH ft.folder JOIN FETCH ft.tag WHERE ft.tag = :sub order by ft.folder.createdAt DESC ")
//    Page<FolderTag> findFolderTagByRootTagOrderByCreatedAt(TagCategory sub);
}
