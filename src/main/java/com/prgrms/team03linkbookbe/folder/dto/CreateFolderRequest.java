package com.prgrms.team03linkbookbe.folder.dto;


import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import com.prgrms.team03linkbookbe.user.entity.User;
import java.util.Set;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // object mapping을 위해
public class CreateFolderRequest {

    @NotBlank(message = "폴더의 이름을 입력해주세요")
    private String name;

    @NotBlank(message = "폴더의 이미지를 설정해주세요")
    private String image;

    @NotBlank(message = "폴더의 내용을 입력해주세요")
    private String content;

    private Long originId;

    @NotBlank(message = "폴더 고정여부를 설정해주세요")
    private Boolean isPinned;

    @NotBlank(message = "폴더 공개여부를 설정해주세요")
    private Boolean isPrivate;

    private Set<TagCategory> tags;

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public Folder toEntity(){
        return Folder.builder()
            .name(this.getName())
            .image(this.getImage())
            .content(this.getContent())
            .originId(this.getOriginId())
            .isPinned(this.getIsPinned())
            .isPrivate(this.getIsPrivate())
            .user(this.user)
            .build();
    }


}
