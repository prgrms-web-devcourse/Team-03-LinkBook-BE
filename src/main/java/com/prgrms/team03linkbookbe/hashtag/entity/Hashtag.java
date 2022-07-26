package com.prgrms.team03linkbookbe.hashtag.entity;

import com.prgrms.team03linkbookbe.folder.entity.Folder;
import javax.persistence.Column;
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
@Table(name = "hashtag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private Field name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", referencedColumnName = "id")
    private Folder folder;

    @Builder
    public Hashtag(Long id, Field name, Folder folder) {
        this.id = id;
        this.name = name;
        this.folder = folder;
    }

}
