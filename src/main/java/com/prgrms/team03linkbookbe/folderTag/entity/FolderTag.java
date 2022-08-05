package com.prgrms.team03linkbookbe.folderTag.entity;


import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.tag.entity.Tag;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "folder_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FolderTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", referencedColumnName = "id")
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", referencedColumnName = "id")
    private Folder folder;


    @Builder
    public FolderTag(Long id, Tag tag, Folder folder) {
        this.id = id;
        this.tag = tag;
        this.folder = folder;
    }
}
