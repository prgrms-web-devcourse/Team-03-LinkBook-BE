package com.prgrms.team03linkbookbe.email.controller;

import com.prgrms.team03linkbookbe.common.response.ExceptionResponse;
import com.prgrms.team03linkbookbe.email.exception.EmailCertificationFailureException;
import com.prgrms.team03linkbookbe.email.exception.EmailSendFailureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice("com.prgrms.team03linkbookbe.email")
public class EmailControllerAdvice {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailSendFailureException.class)
    public ExceptionResponse handleEmailSendFailureException(EmailSendFailureException e) {
        log.info("email send failure exception", e);
        return new ExceptionResponse(e.getExceptionCode());
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailCertificationFailureException.class)
    public ExceptionResponse handleEmailCertificationFailureException(EmailCertificationFailureException e) {
        log.info("email certification failure exception", e);
        return new ExceptionResponse(e.getExceptionCode());
    }

}
