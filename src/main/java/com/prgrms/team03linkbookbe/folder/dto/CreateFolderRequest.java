package com.prgrms.team03linkbookbe.folder.dto;


import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import com.prgrms.team03linkbookbe.user.entity.User;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // object mapping을 위해
public class CreateFolderRequest {

    @NotBlank(message = "폴더의 이름을 입력해주세요")
    private String title;

    @NotBlank(message = "폴더의 이미지를 설정해주세요")
    private String image;

    @NotBlank(message = "폴더의 내용을 입력해주세요")
    private String content;

    private Long originId;

    @NotNull(message = "폴더 고정여부를 설정해주세요")
    private Boolean isPinned;

    @NotNull(message = "폴더 공개여부를 설정해주세요")
    private Boolean isPrivate;

    private Set<TagCategory> tags;

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public Folder toEntity() {
        return Folder.builder()
            .title(this.getTitle())
            .image(this.getImage())
            .content(this.getContent())
            .originId(this.getOriginId())
            .isPinned(this.getIsPinned())
            .isPrivate(this.getIsPrivate())
            .user(this.user)
            .build();
    }


}
