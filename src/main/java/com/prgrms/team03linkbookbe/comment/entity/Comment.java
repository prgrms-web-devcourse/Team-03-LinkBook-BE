package com.prgrms.team03linkbookbe.comment.entity;

import com.prgrms.team03linkbookbe.common.entity.BaseDateEntity;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id", nullable = true)
    private Long parentId;

    @Size(min = 1, max = 1000, message = "댓글은 1~1000자 까지 가능합니다.")
    @Column(name = "content", nullable = false, columnDefinition = "varchar(1000)")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", referencedColumnName = "id")
    private Folder folder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private User user;

    @Builder(toBuilder = true)
    public Comment(Long id, Long parentId, String content,
                   Folder folder, User user) {
        this.id = id;
        this.parentId = parentId;
        this.content = content;
        this.folder = folder;
        this.user = user;
    }

}
