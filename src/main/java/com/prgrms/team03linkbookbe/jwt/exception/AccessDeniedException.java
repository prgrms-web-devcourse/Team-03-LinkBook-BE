package com.prgrms.team03linkbookbe.jwt.exception;

import com.prgrms.team03linkbookbe.common.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class AccessDeniedException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public AccessDeniedException() {
        this.exceptionCode = ExceptionCode.ACCESS_DENIED;
    }
}
