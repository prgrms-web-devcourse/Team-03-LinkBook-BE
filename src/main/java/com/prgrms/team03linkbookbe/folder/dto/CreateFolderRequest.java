package com.prgrms.team03linkbookbe.folder.dto;


import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.tag.entity.Tag;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateFolderRequest {

    @NotBlank(message = "폴더의 이름을 입력해주세요")
    private String name;

    @NotBlank(message = "폴더의 이미지를 설정해주세요")
    private String image;

    @NotBlank(message = "폴더의 내용을 입력해주세요")
    private String content;

    @Column(name = "origin_id", nullable = true)
    private Long originId;

    @Column(name = "is_pinned", nullable = false)
    private Boolean isPinned;

    @Column(name = "is_private", nullable = false)
    private Boolean isPrivate;


    private Set<TagCategory> tags;

    public Folder toEntity(){
        return Folder.builder()
            .name(this.getName())
            .image(this.getImage())
            .content(this.getContent())
            .originId(this.getOriginId())
            .isPinned(this.getIsPinned())
            .isPrivate(this.getIsPrivate())
            .build();
    }


}
