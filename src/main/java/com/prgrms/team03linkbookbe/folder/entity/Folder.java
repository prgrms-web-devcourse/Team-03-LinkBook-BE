package com.prgrms.team03linkbookbe.folder.entity;

import com.prgrms.team03linkbookbe.common.entity.BaseDateEntity;
import com.prgrms.team03linkbookbe.user.entity.User;
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
@Table(name = "folder")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Folder extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(20)")
    private String name;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "content", nullable = false, columnDefinition = "varchar(10000)")
    private String content;

    @Column(name = "origin_id", nullable = false)
    private Long originId;

    @Column(name = "is_main", nullable = false)
    private Boolean isMain;

    @Column(name = "is_private", nullable = false)
    private Boolean isPrivate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private User user;

    @Builder
    public Folder(Long id, String name, String image, String content, Long originId,
        Boolean isMain, Boolean isPrivate, User user) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.content = content;
        this.originId = originId;
        this.isMain = isMain;
        this.isPrivate = isPrivate;
        this.user = user;
    }
}
