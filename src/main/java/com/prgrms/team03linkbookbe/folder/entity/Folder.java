package com.prgrms.team03linkbookbe.folder.entity;

import com.prgrms.team03linkbookbe.comment.entity.Comment;
import com.prgrms.team03linkbookbe.common.entity.BaseDateEntity;
import com.prgrms.team03linkbookbe.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "origin_id", nullable = true)
    private Long originId;

    @Column(name = "is_main", nullable = false)
    private Boolean isMain;

    @Column(name = "is_private", nullable = false)
    private Boolean isPrivate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "folder")
    private List<Comment> comments = new ArrayList<>();

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
