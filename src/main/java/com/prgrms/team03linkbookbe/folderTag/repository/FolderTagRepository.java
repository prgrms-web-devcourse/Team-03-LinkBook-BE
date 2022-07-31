package com.prgrms.team03linkbookbe.folderTag.repository;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folderTag.entity.FolderTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderTagRepository extends JpaRepository<FolderTag, Long> {

    void deleteAllByFolder(Folder folder);
}
