package com.prgrms.team03linkbookbe.user.exception;

import com.prgrms.team03linkbookbe.common.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class IllegalPasswordException extends RuntimeException{

    private final ExceptionCode exceptionCode;

    public IllegalPasswordException() {
        this.exceptionCode = ExceptionCode.ILLEGAL_PASSWORD_REQUEST;
    }
}