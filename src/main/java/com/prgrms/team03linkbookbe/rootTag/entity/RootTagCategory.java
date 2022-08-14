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
        TagCategory.DAILY1,
        TagCategory.HOBBY,
        TagCategory.LIFEHACK
    }),
    ANIMAL("동물", new TagCategory[]{
        TagCategory.ANIMAL1,
        TagCategory.DOG,
        TagCategory.CAT,
        TagCategory.REPTILE,
        TagCategory.INSECT
    }),
    GAME("게임", new TagCategory[]{
        TagCategory.GAME1,
        TagCategory.FPS,
        TagCategory.RPG,
        TagCategory.TPS,
        TagCategory.SPORTS_GAME,
        TagCategory.RACING_GAME,
        TagCategory.SIMULATION_GAME,
        TagCategory.RHYTHM_GAME
    }),
    MOVIE("영화", new TagCategory[]{
        TagCategory.MOVIE1,
        TagCategory.ROMANCE_MOVIE,
        TagCategory.ACTION_MOVIE,
        TagCategory.CRIME_MOVIE,
        TagCategory.SF_MOVIE,
        TagCategory.COMEDY_MOVIE,
        TagCategory.THRILLER_MOVIE,
        TagCategory.HORROR_MOVIE,
        TagCategory.SPORTS_MOVIE,
        TagCategory.FANTASY_MOVIE
    }),
    MUSIC("음악", new TagCategory[]{
        TagCategory.MUSIC1,
        TagCategory.POP,
        TagCategory.JPOP,
        TagCategory.BALLADE,
        TagCategory.RAP,
        TagCategory.BAND,
        TagCategory.ACOUSTIC,
        TagCategory.JAZZ
    }),
    HUMOR("유머", new TagCategory[]{
        TagCategory.HUMOR1,
        TagCategory.BLACK_COMEDY,
        TagCategory.DAD_JOKE,
        TagCategory.SATIRE
    }),
    HEALTH("헬스", new TagCategory[]{
        TagCategory.HEALTH1,
        TagCategory.WORKOUT,
        TagCategory.PILATES,
        TagCategory.POWER_LIFTING,
        TagCategory.CROSSFIT
    }),
    BEAUTY("뷰티", new TagCategory[]{
        TagCategory.BEAUTY1,
        TagCategory.MAKEUP,
        TagCategory.CLOTHES
    }),
    SPORTS("스포츠", new TagCategory[]{
        TagCategory.SPORTS1,
        TagCategory.SOCCER,
        TagCategory.BASKETBALL,
        TagCategory.BASEBALL,
        TagCategory.BILLIARDS,
        TagCategory.BOWLING,
        TagCategory.VOLLEYBALL
    }),
    DEVELOP("개발", new TagCategory[]{
        TagCategory.DEVELOP1,
        TagCategory.FRONTEND,
        TagCategory.BACKEND,
        TagCategory.INFRA,
        TagCategory.DEVOPS,
        TagCategory.DATA_VISUALIZATION,
        TagCategory.AI
    }),
    TRAVEL("여행", new TagCategory[]{
        TagCategory.TRAVEL1,
        TagCategory.DOMESTIC_TRAVEL,
        TagCategory.OVERSEAS_TRAVEL,
        TagCategory.BACKPACKING
    }),
    FOOD("음식", new TagCategory[]{
        TagCategory.FOOD1,
        TagCategory.KOREAN_FOOD,
        TagCategory.JAPANESE_FOOD,
        TagCategory.CHINESE_FOOD,
        TagCategory.VIETNAMESE_FOOD,
        TagCategory.WESTERN_FOOD,
        TagCategory.DESSERT
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
