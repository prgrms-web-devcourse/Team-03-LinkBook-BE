package com.prgrms.team03linkbookbe.tag.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TagCategory {
    // 일상
    HOBBY("취미",null),
    LIFEHACK("꿀팁",null),

    DAILY("일상", new TagCategory[]{
        HOBBY, LIFEHACK
    }),

    // 동물
    DOG("개",null),
    CAT("고양이",null),
    REPTILE("파충류",null),
    INSECT("곤충",null),

    ANIMAL("동물", new TagCategory[]{
        DOG, CAT, REPTILE, INSECT
    }),

    // 게임
    FPS("FPS",null),
    RPG("RPG",null),
    TPS("TPS",null),
    SPORTS_GAME("스포츠 게임",null),
    RACING_GAME("레이싱 게임",null),
    SIMULATION_GAME("시뮬레이션 게임",null),
    RHYTHM_GAME("리듬 게임",null),

    GAME("게임", new TagCategory[]{
        FPS, RPG, TPS, SPORTS_GAME, RACING_GAME, SIMULATION_GAME, RHYTHM_GAME
    }),

    // 영화
    ROMANCE_MOVIE("로맨스",null),
    ACTION_MOVIE("액션",null),
    CRIME_MOVIE("범죄",null),
    SF_MOVIE("SF",null),
    COMEDY_MOVIE("코미디",null),
    THRILLER_MOVIE("스릴러",null),
    HORROR_MOVIE("공포",null),
    SPORTS_MOVIE("스포츠",null),
    FANTASY_MOVIE("판타지",null),

    MOVIE("영화", new TagCategory[]{
        ROMANCE_MOVIE, ACTION_MOVIE, CRIME_MOVIE, SF_MOVIE, COMEDY_MOVIE, THRILLER_MOVIE, HORROR_MOVIE, SPORTS_MOVIE, FANTASY_MOVIE
    }),

    // 음악
    POP("pop",null),
    JPOP("jpop",null),
    BALLADE("발라드",null),
    RAP("랩",null),
    BAND("밴드",null),
    ACOUSTIC("어쿠스틱",null),
    JAZZ("재즈",null),

    MUSIC("음악", new TagCategory[]{
        POP, JPOP, BALLADE, RAP, BAND, ACOUSTIC, JAZZ
    }),

    // 유머
    BLACK_COMEDY("블랙 코미디",null),
    DAD_JOKE("아재 개그",null),
    SATIRE("풍자",null),

    HUMOR("유머", new TagCategory[]{
        BLACK_COMEDY, DAD_JOKE, SATIRE
    }),

    // 헬스
    WORKOUT("헬스",null),
    PILATES("필라테스",null),
    POWER_LIFTING("파워 리프팅",null),
    CROSSFIT("크로스핏",null),

    HEALTH("헬스", new TagCategory[]{
        WORKOUT, PILATES, POWER_LIFTING, CROSSFIT
    }),


    // 뷰티
    MAKEUP("화장",null),
    CLOTHES("옷",null),

    BEAUTY("뷰티", new TagCategory[]{
        MAKEUP, CLOTHES
    }),

    // 스포츠
    SOCCER("축구",null),
    BASKETBALL("농구",null),
    BASEBALL("야구",null),
    BILLIARDS("당구",null),
    BOWLING("볼링",null),
    VOLLEYBALL("배구",null),

    SPORTS("스포츠", new TagCategory[]{
        SOCCER, BASKETBALL, BASEBALL, BILLIARDS, BOWLING, VOLLEYBALL
    }),

    // 개발
    FRONTEND("프론트엔드",null),
    BACKEND("백엔드",null),
    INFRA("인프라",null),
    DEVOPS("데브옵스",null),
    DATA_VISUALIZATION("데이터 시각화",null),
    AI("인공지능",null),

    DEVELOP("개발", new TagCategory[]{
        FRONTEND, BACKEND, INFRA, DEVOPS, DATA_VISUALIZATION, AI
    }),

    // 여행
    DOMESTIC_TRAVEL("국내 여행",null),
    OVERSEAS_TRAVEL("해외 여행",null),
    BACKPACKING("배낭 여행",null),

    TRAVEL("여행", new TagCategory[]{
        DOMESTIC_TRAVEL, OVERSEAS_TRAVEL, BACKPACKING
    }),

    // 음식
    KOREAN_FOOD("한식",null),
    CHINESE_FOOD("중식",null),
    JAPANESE_FOOD("일식",null),
    VIETNAMESE_FOOD("베트남",null),
    WESTERN_FOOD("양식",null),
    DESSERT("디저트",null),

    FOOD("음식", new TagCategory[]{
        KOREAN_FOOD, JAPANESE_FOOD, CHINESE_FOOD, VIETNAMESE_FOOD, WESTERN_FOOD, DESSERT
    });

    private String viewName;

    private TagCategory[] tags;

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
