package com.prgrms.team03linkbookbe.common.exception;

import lombok.Getter;

@Getter
public enum ExceptionCode {

    // entity 이름
    NO_DATA_IN_DB(0, "데이터베이스에 값이 존재하지 않습니다."),
    BEAN_VALIDATION_TYPE_MISMATCH(1,"입력타입이 일치하지 않습니다."),

    // User
    ILLEGAL_TOKEN(-100, "비정상적인 접근입니다."),
    EXPIRED_ACCESS_TOKEN(-101, "AccessToken 유효기한이 만료되었습니다."),
    EXPIRED_REFRESH_TOKEN(-102, "RefreshToken 유효기한이 만료되었습니다."),
    ACCESS_DENIED(-103, "접근 권한이 없습니다."),


    LOGIN_FAILURE(-110, "로그인에 실패하였습니다."),
    DUPLICATED_EMAIL(-111, "중복된 이메일 입니다."),

    EMAIL_SEND_FAILURE(-120, "이메일 전송에 실패하였습니다."),
    EMAIL_CERTIFICATION_FAILURE(-121, "이메일 인증에 실패하였습니다."),
    EMAIL_IS_NOT_CERTIFICATED(-122, "아직 인증되지 않은 이메일 입니다."),

    ILLEGAL_PASSWORD_REQUEST(-130, "비밀번호는 영문자, 숫자를 모두 포함해서 입력해주세요."),

    // Folder
    ILLEGAL_ACCESS_TO_PRIVATE_FOLDER(-300, "Private 폴더에 접근할 권한이 없습니다.");

    private final int code;
    private final String msg;

    ExceptionCode(int code, String message) {
        this.code = code;
        this.msg = message;
    }

}
