package com.prgrms.team03linkbookbe.interest.entity;

public enum Field {
    backEndDeveloper("백엔드 개발자"),
    frontEndDeveloper("프론트엔드 개발자");

    private final String field;

    Field(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

}
