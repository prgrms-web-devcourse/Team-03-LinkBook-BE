package com.prgrms.team03linkbookbe.interest.entity;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Field {
    backEndDeveloper("백엔드 개발자"),
    frontEndDeveloper("프론트엔드 개발자");

    private static final Map<String, Field> ENUM_MAP =
        Stream.of(values()).collect(Collectors.toMap(Field::getField, o -> o));

    private final String field;

    Field(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public static Field getEnum(String field) {
        return ENUM_MAP.get(field);
    }

}
