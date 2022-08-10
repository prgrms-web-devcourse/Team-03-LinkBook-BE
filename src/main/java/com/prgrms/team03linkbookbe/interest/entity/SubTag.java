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

    public static final Map<String, SubTag> ENUM_MAP =
        Stream.of(values()).collect(Collectors.toMap(SubTag::getViewName, subTag -> subTag));

    public static SubTag getEnum(String subTag) {
        if(!ENUM_MAP.containsKey(subTag)) {
            throw new IllegalArgumentException("존재하지 않는 Tag를 입력하였습니다.");
        }
        return ENUM_MAP.get(subTag);
    }
}
