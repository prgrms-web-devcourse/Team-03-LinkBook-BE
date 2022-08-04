package com.prgrms.team03linkbookbe.user.exception;

import com.prgrms.team03linkbookbe.common.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class LoginFailureException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public LoginFailureException() {
        this.exceptionCode = ExceptionCode.LOGIN_FAILURE;
    }

}
