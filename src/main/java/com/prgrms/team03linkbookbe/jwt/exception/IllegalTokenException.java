package com.prgrms.team03linkbookbe.jwt.exception;

import com.prgrms.team03linkbookbe.common.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class IllegalTokenException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public IllegalTokenException() {
        this.exceptionCode = ExceptionCode.ILLEGAL_TOKEN;
    }

}
