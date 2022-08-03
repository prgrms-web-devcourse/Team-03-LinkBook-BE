package com.prgrms.team03linkbookbe.folder.entity;

import com.prgrms.team03linkbookbe.bookmark.entity.Bookmark;
import com.prgrms.team03linkbookbe.comment.entity.Comment;
import com.prgrms.team03linkbookbe.common.entity.BaseDateEntity;
import com.prgrms.team03linkbookbe.folder.dto.CreateFolderRequest;
import com.prgrms.team03linkbookbe.folderTag.entity.FolderTag;
import com.prgrms.team03linkbookbe.user.entity.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder(toBuilder = true)
@Table(name = "folder")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Folder extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50, message = "폴더의 이름은 50자 이하로 입력해주세요")
    @NotBlank(message = "폴더의 이름을 입력해주세요")
    @Column(name = "name", nullable = false, columnDefinition = "varchar(50)")
    private String name;

    @NotBlank(message = "폴더의 이미지를 설정해주세요")
    @Column(name = "image", nullable = false, columnDefinition = "varchar(1000)")
    private String image;

    @NotBlank(message = "폴더의 내용을 입력해주세요")
    @Column(name = "content", nullable = false, columnDefinition = "varchar(10000)")
    private String content;

    @Column(name = "origin_id", nullable = true)
    private Long originId;

    @NotNull(message = "폴더의 핀여부를 선택해주세요")
    @Column(name = "is_pinned", nullable = false)
    private Boolean isPinned;

    @NotNull(message = "폴더의 공개여부를 선택해주세요")
    @Column(name = "is_private", nullable = false)
    private Boolean isPrivate;

    @NotNull(message = "좋아요수를 넣어주세요.")
    @Column(name = "likes", nullable = false)
    private Integer likes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "folder")
    private List<Bookmark> bookmarks = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "folder")
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "folder")
    private List<FolderTag> folderTags = new ArrayList<>();

    public void modifyFolder(CreateFolderRequest dto) {
        this.name = dto.getName();
        this.image = dto.getImage();
        this.isPinned = dto.getIsPinned();
        this.isPrivate = dto.getIsPrivate();
        this.content = dto.getContent();
        this.originId = dto.getOriginId();
    }
}