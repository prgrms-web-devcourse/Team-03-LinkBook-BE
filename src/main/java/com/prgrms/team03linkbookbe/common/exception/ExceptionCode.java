package com.prgrms.team03linkbookbe.common.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    // entity 이름
    NO_DATA_IN_DB(-100, "데이터베이스에 값이 존재하지 않습니다.");

    private final int code;
    private final String msg;

    ExceptionCode(int code, String message) {
        this.code = code;
        this.msg = message;
    }

}
