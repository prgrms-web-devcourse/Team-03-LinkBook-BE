package com.prgrms.team03linkbookbe.rootTag.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RootTagCategory {
    ALL("전체 카테고리", new TagCategory[0]),
    DAILY("일상", new TagCategory[]{
        TagCategory.DAILY1
    }),
    ANIMAL("동물", new TagCategory[]{
        TagCategory.ANIMAL1
    }),
    GAME("게임", new TagCategory[]{
        TagCategory.GAME1
    }),
    MOVIE("영화", new TagCategory[]{
        TagCategory.MOVIE1
    }),
    MUSIC("음악", new TagCategory[]{
        TagCategory.MUSIC1
    }),
    HUMOR("유머", new TagCategory[]{
        TagCategory.HUMOR1
    }),
    HEALTH("헬스", new TagCategory[]{
        TagCategory.HEALTH1
    }),
    BEAUTY("뷰티", new TagCategory[]{
        TagCategory.BEAUTY1
    }),
    SPORTS("스포츠", new TagCategory[]{
        TagCategory.SPORTS1
    }),
    DEVELOP("개발", new TagCategory[]{
        TagCategory.DEVELOP1
    }),
    TRAVEL("여행", new TagCategory[]{
        TagCategory.TRAVEL1
    }),
    FOOD("음식", new TagCategory[]{
        TagCategory.FOOD1
    });

    private String viewName;

    private TagCategory[] tags;

    public static boolean hasRootTag(RootTagCategory root, TagCategory target) {
        return Arrays.stream(root.getTags())
            .anyMatch(tag -> tag == target);
    }

    @JsonCreator
    public static RootTagCategory from(String root) {
        for (RootTagCategory tag : RootTagCategory.values()) {
            if (tag.getViewName().equals(root)) {
                return tag;
            }
        }
        return null;
    }

}
