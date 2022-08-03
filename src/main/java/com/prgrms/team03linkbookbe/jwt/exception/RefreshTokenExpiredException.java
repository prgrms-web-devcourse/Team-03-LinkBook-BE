package com.prgrms.team03linkbookbe.jwt.exception;

import com.prgrms.team03linkbookbe.common.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class RefreshTokenExpiredException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public RefreshTokenExpiredException() {
        this.exceptionCode = ExceptionCode.EXPIRED_REFRESH_TOKEN;
    }

}
