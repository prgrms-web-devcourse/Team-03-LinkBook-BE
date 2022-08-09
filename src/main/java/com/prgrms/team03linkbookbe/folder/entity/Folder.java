package com.prgrms.team03linkbookbe.folder.entity;

import com.prgrms.team03linkbookbe.bookmark.entity.Bookmark;
import com.prgrms.team03linkbookbe.comment.entity.Comment;
import com.prgrms.team03linkbookbe.common.entity.BaseDateEntity;
import com.prgrms.team03linkbookbe.folder.dto.CreateFolderRequest;
import com.prgrms.team03linkbookbe.folderTag.entity.FolderTag;
import com.prgrms.team03linkbookbe.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    @Column(name = "title", nullable = false, columnDefinition = "varchar(50)")
    private String title;

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

    @PositiveOrZero(message = "좋아요는 0이상 값으로 넣어주세요.")
    @NotNull(message = "좋아요수를 넣어주세요.")
    @Column(name = "likes", nullable = false)
    private int likes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "folder", cascade = CascadeType.REMOVE)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "folder", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "folder", cascade = CascadeType.REMOVE)
    private List<FolderTag> folderTags = new ArrayList<>();

    public void modifyFolder(CreateFolderRequest dto) {
        this.title = dto.getTitle();
        this.image = dto.getImage();
        this.isPinned = dto.getIsPinned();
        this.isPrivate = dto.getIsPrivate();
        this.content = dto.getContent();
        this.originId = dto.getOriginId();
    }

    public Folder updateFolderTags(List<FolderTag> folderTags) {
        this.folderTags = folderTags;
        return this;
    }
}