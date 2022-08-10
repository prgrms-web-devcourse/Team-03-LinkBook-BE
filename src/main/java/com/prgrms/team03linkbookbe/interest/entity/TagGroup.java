package com.prgrms.team03linkbookbe.interest.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.prgrms.team03linkbookbe.common.exception.NoDataException;
import com.prgrms.team03linkbookbe.rootTag.entity.RootTagCategory;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TagGroup {

    DAILY("일상", Arrays.asList(SubTag.DAILY1)),
    ANIMAL("동물", Arrays.asList(SubTag.ANIMAL1)),
    GAME("게임", Arrays.asList(SubTag.GAME1)),
    MOVIE("영화", Arrays.asList(SubTag.MOVIE1)),
    MUSIC("음악",  Arrays.asList(SubTag.MUSIC1)),
    HUMOR("유머",  Arrays.asList(SubTag.HUMOR1)),
    HEALTH("헬스",  Arrays.asList(SubTag.HEALTH1)),
    BEAUTY("뷰티",  Arrays.asList(SubTag.BEAUTY1)),
    SPORTS("스포츠",  Arrays.asList(SubTag.SPORTS1)),
    DEVELOP("개발",  Arrays.asList(SubTag.DEVELOP1)),
    TRAVEL("여행",  Arrays.asList(SubTag.TRAVEL1)),
    FOOD("음식",  Arrays.asList(SubTag.FOOD1));

    private String viewName;

    private List<SubTag> subTags;

    public static TagGroup findBySubTag(SubTag subTag) {
        return Arrays.stream(TagGroup.values())
            .filter(tagGroup -> tagGroup.hasSubTag(subTag))
            .findAny()
            .orElseThrow(() -> new NoDataException());
    }

    public boolean hasSubTag(SubTag subTag){
        return subTags.stream()
            .anyMatch(tag -> tag.equals(subTag));
    }

//    @JsonCreator
//    public static RootTagCategory from(String root){
//        for (RootTagCategory tag : RootTagCategory.values()) {
//            if (tag.getViewName().equals(root)) {
//                return tag;
//            }
//        }
//        return null;
//    }
}
