package com.prgrms.team03linkbookbe.jwt.exception;

import com.prgrms.team03linkbookbe.common.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class AccessTokenExpiredException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public AccessTokenExpiredException() {
        this.exceptionCode = ExceptionCode.ACCESS_DENIED;
    }
}
