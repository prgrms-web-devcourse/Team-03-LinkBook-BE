package com.prgrms.team03linkbookbe.user.controller;

import com.prgrms.team03linkbookbe.common.response.ExceptionResponse;
import com.prgrms.team03linkbookbe.user.exception.DuplicatedEmailException;
import com.prgrms.team03linkbookbe.user.exception.LoginFailureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice("com.prgrms.team03linkbookbe.user")
public class UserControllerAdvice {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicatedEmailException.class)
    public ExceptionResponse handleDuplicatedEmailException(DuplicatedEmailException e) {
        log.info("duplicated email exception", e);
        return new ExceptionResponse(e.getExceptionCode());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LoginFailureException.class)
    public ExceptionResponse handleLoginFailureException(LoginFailureException e) {
        log.info("login failure exception", e);
        return new ExceptionResponse(e.getExceptionCode());
    }



}
