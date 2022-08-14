package com.prgrms.team03linkbookbe.interest.entity;

import com.prgrms.team03linkbookbe.common.exception.NoDataException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubTag {
    // 일상
    DAILY1("일상1"),
    HOBBY("취미"),
    LIFEHACK("꿀팁"),

    // 동물
    ANIMAL1("동물1"),
    DOG("개"),
    CAT("고양이"),
    REPTILE("파충류"),
    INSECT("곤충"),

    // 게임
    GAME1("게임1"),
    FPS("FPS"),
    RPG("RPG"),
    TPS("TPS"),
    SPORTS_GAME("스포츠 게임"),
    RACING_GAME("레이싱 게임"),
    SIMULATION_GAME("시뮬레이션 게임"),
    RHYTHM_GAME("리듬 게임"),

    // 영화
    MOVIE1("영화1"),
    ROMANCE_MOVIE("로맨스"),
    ACTION_MOVIE("액션"),
    CRIME_MOVIE("범죄"),
    SF_MOVIE("SF"),
    COMEDY_MOVIE("코미디"),
    THRILLER_MOVIE("스릴러"),
    HORROR_MOVIE("공포"),
    SPORTS_MOVIE("스포츠"),
    FANTASY_MOVIE("판타지"),


    // 음악
    MUSIC1("음악1"),
    POP("pop"),
    JPOP("jpop"),
    BALLADE("발라드"),
    RAP("랩"),
    BAND("밴드"),
    ACOUSTIC("어쿠스틱"),
    JAZZ("재즈"),

    // 유머
    HUMOR1("유머1"),
    BLACK_COMEDY("블랙 코미디"),
    DAD_JOKE("아재 개그"),
    SATIRE("풍자"),

    // 헬스
    HEALTH1("헬스1"),
    WORKOUT("헬스"),
    PILATES("필라테스"),
    POWER_LIFTING("파워 리프팅"),
    CROSSFIT("크로스핏"),

    // 뷰티
    BEAUTY1("뷰티1"),
    MAKEUP("화장"),
    CLOTHES("옷"),

    // 스포츠
    SPORTS1("스포츠1"),
    SOCCER("축구"),
    BASKETBALL("농구"),
    BASEBALL("야구"),
    BILLIARDS("당구"),
    BOWLING("볼링"),
    VOLLEYBALL("배구"),

    // 개발
    DEVELOP1("개발1"),
    FRONTEND("프론트엔드"),
    BACKEND("백엔드"),
    INFRA("인프라"),
    DEVOPS("데브옵스"),
    DATA_VISUALIZATION("데이터 시각화"),
    AI("인공지능"),

    // 여행
    TRAVEL1("여행1"),
    DOMESTIC_TRAVEL("국내 여행"),
    OVERSEAS_TRAVEL("해외 여행"),
    BACKPACKING("배낭 여행"),

    // 음식
    FOOD1("음식1"),
    KOREAN_FOOD("한식"),
    CHINESE_FOOD("중식"),
    JAPANESE_FOOD("일식"),
    VIETNAMESE_FOOD("베트남"),
    WESTERN_FOOD("양식"),
    DESSERT("디저트");

    private String viewName;

    public static final Map<String, SubTag> ENUM_MAP =
        Stream.of(values()).collect(Collectors.toMap(SubTag::getViewName, subTag -> subTag));

    public static SubTag getEnum(String subTag) {
        if(!ENUM_MAP.containsKey(subTag)) {
            throw new IllegalArgumentException("존재하지 않는 Tag를 입력하였습니다.");
        }
        return ENUM_MAP.get(subTag);
    }
}
