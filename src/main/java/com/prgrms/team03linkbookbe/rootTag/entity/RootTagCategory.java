package com.prgrms.team03linkbookbe.rootTag.entity;

import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RootTagCategory {
    DAILY1("일상", new TagCategory[]{
        TagCategory.DAILY1
    }),
    ANIMAL1("동물", new TagCategory[]{
        TagCategory.ANIMAL1
    }),
    GAME1("게임", new TagCategory[]{
        TagCategory.GAME1
    }),
    MOVIE1("영화", new TagCategory[]{
       TagCategory.MOVIE1
    }),
    MUSIC1("음악",  new TagCategory[]{
        TagCategory.MUSIC1
    }),
    HUMOR1("유머",  new TagCategory[]{
        TagCategory.HUMOR1
    }),
    HEALTH1("헬스",  new TagCategory[]{
        TagCategory.HEALTH1
    }),
    BEAUTY1("뷰티",  new TagCategory[]{
        TagCategory.BEAUTY1
    }),
    SPORTS1("스포츠",  new TagCategory[]{
        TagCategory.SPORTS1
    }),
    DEVELOP1("개발",  new TagCategory[]{
        TagCategory.DEVELOP1
    }),
    TRAVEL1("여행",  new TagCategory[]{
        TagCategory.TRAVEL1
    }),
    FOOD1("음식",  new TagCategory[]{
        TagCategory.FOOD1
    });

    private String viewName;

    private TagCategory[] tags;

    public static boolean hasRootTag(RootTagCategory root, TagCategory target){
        return Arrays.stream(root.getTags())
                        .anyMatch(tag -> tag == target);
    }

}
