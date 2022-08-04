package com.prgrms.team03linkbookbe.common.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    // entity 이름
    NO_DATA_IN_DB(0, "데이터베이스에 값이 존재하지 않습니다."),

    // User
    ILLEGAL_TOKEN(-100, "유효하지 않은 Token 입니다."),
    EXPIRED_ACCESS_TOKEN(-101, "AccessToken 유효기한이 만료되었습니다."),
    EXPIRED_REFRESH_TOKEN(-102, "RefreshToken 유효기한이 만료되었습니다."),
    ACCESS_DENIED(-103, "접근 권한이 없습니다."),


    LOGIN_FAILURE(-110, "로그인에 실패하였습니다."),
    DUPLICATED_EMAIL(-111, "중복된 이메일 입니다.");

    private final int code;
    private final String msg;

    ExceptionCode(int code, String message) {
        this.code = code;
        this.msg = message;
    }

}
