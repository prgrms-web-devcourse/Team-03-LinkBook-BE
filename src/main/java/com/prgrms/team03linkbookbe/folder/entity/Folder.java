package com.prgrms.team03linkbookbe.folder.entity;

import com.prgrms.team03linkbookbe.bookmark.entity.Bookmark;
import com.prgrms.team03linkbookbe.comment.entity.Comment;
import com.prgrms.team03linkbookbe.common.entity.BaseDateEntity;
import com.prgrms.team03linkbookbe.user.entity.User;
import java.util.ArrayList;
import java.util.List;
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
import javax.validation.constraints.Size;
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

    @NotBlank(message = "폴더의 핀여부를 선택해주세요")
    @Column(name = "is_pinned", nullable = false)
    private Boolean isPinned;

    @NotBlank(message = "폴더의 공개여부를 선택해주세요")
    @Column(name = "is_private", nullable = false)
    private Boolean isPrivate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "folder")
    private List<Bookmark> bookmarks= new ArrayList<>();

    @OneToMany(mappedBy = "folder")
    private List<Comment> comments = new ArrayList<>();



    @Builder
    public Folder(Long id, String name, String image, String content, Long originId,
        Boolean isPinned, Boolean isPrivate, User user) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.content = content;
        this.originId = originId;
        this.isPinned = isPinned;
        this.isPrivate = isPrivate;
        this.user = user;
    }
}
