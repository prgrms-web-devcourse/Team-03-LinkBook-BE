package com.prgrms.team03linkbookbe.tag.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TagCategory {
    DAILY1("일상1"),
    ANIMAL1("동물1"),
    GAME1("게임1"),
    MOVIE1("영화1"),
    MUSIC1("음악1"),
    HUMOR1("유머1"),
    HEALTH1("헬스1"),
    BEAUTY1("뷰티1"),
    SPORTS1("스포츠1"),
    DEVELOP1("개발1"),
    TRAVEL1("여행1"),
    FOOD1("음식1");

    private String viewName;

    @JsonCreator
    public static TagCategory from(String sub) {
        for (TagCategory tag : TagCategory.values()) {
            if (tag.getViewName().equals(sub)) {
                return tag;
            }
        }
        return null;
    }

}
