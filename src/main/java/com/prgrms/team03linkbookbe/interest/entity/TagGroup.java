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

    DAILY("일상", Arrays.asList(
        SubTag.HOBBY,
        SubTag.LIFEHACK
    )),
    ANIMAL("동물", Arrays.asList(
        SubTag.DOG,
        SubTag.CAT,
        SubTag.REPTILE,
        SubTag.INSECT
    )),
    GAME("게임", Arrays.asList(
        SubTag.FPS,
        SubTag.RPG,
        SubTag.TPS,
        SubTag.SPORTS_GAME,
        SubTag.RACING_GAME,
        SubTag.SIMULATION_GAME,
        SubTag.RHYTHM_GAME)),
    MOVIE("영화", Arrays.asList(
        SubTag.ROMANCE_MOVIE,
        SubTag.ACTION_MOVIE,
        SubTag.CRIME_MOVIE,
        SubTag.SF_MOVIE,
        SubTag.COMEDY_MOVIE,
        SubTag.THRILLER_MOVIE,
        SubTag.HORROR_MOVIE,
        SubTag.SPORTS_MOVIE,
        SubTag.FANTASY_MOVIE
    )),
    MUSIC("음악",  Arrays.asList(
        SubTag.POP,
        SubTag.JPOP,
        SubTag.BALLADE,
        SubTag.RAP,
        SubTag.BAND,
        SubTag.ACOUSTIC,
        SubTag.JAZZ
    )),
    HUMOR("유머",  Arrays.asList(
        SubTag.BLACK_COMEDY,
        SubTag.DAD_JOKE,
        SubTag.SATIRE
    )),
    HEALTH("헬스",  Arrays.asList(
        SubTag.WORKOUT,
        SubTag.PILATES,
        SubTag.POWER_LIFTING,
        SubTag.CROSSFIT
    )),
    BEAUTY("뷰티",  Arrays.asList(
        SubTag.MAKEUP,
        SubTag.CLOTHES
    )),
    SPORTS("스포츠",  Arrays.asList(
        SubTag.SOCCER,
        SubTag.BASKETBALL,
        SubTag.BASEBALL,
        SubTag.BILLIARDS,
        SubTag.BOWLING,
        SubTag.VOLLEYBALL
    )),
    DEVELOP("개발",  Arrays.asList(
        SubTag.FRONTEND,
        SubTag.BACKEND,
        SubTag.INFRA,
        SubTag.DEVOPS,
        SubTag.DATA_VISUALIZATION,
        SubTag.AI
    )),
    TRAVEL("여행",  Arrays.asList(
        SubTag.DOMESTIC_TRAVEL,
        SubTag.OVERSEAS_TRAVEL,
        SubTag.BACKPACKING
    )),
    FOOD("음식",  Arrays.asList(
        SubTag.KOREAN_FOOD,
        SubTag.CHINESE_FOOD,
        SubTag.JAPANESE_FOOD,
        SubTag.VIETNAMESE_FOOD,
        SubTag.WESTERN_FOOD,
        SubTag.DESSERT
    ));

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

}
