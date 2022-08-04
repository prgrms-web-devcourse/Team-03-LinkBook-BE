package com.prgrms.team03linkbookbe.common.controller;

import com.prgrms.team03linkbookbe.common.exception.NoDataException;
import com.prgrms.team03linkbookbe.common.response.ExceptionResponse;
import com.prgrms.team03linkbookbe.jwt.exception.AccessTokenExpiredException;
import com.prgrms.team03linkbookbe.jwt.exception.IllegalTokenException;
import com.prgrms.team03linkbookbe.jwt.exception.RefreshTokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoDataException.class)
    public ExceptionResponse handleNotFoundException(NoDataException ex) {
        log.info("not found data exception", ex);
        return new ExceptionResponse(ex.getExceptionCode());
    }


    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessTokenExpiredException.class)
    public ExceptionResponse handleAccessTokenExpiredException(AccessTokenExpiredException ex) {
        log.info("accessToken expired exception ", ex);
        return new ExceptionResponse(ex.getExceptionCode());
    }


    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(IllegalTokenException.class)
    public ExceptionResponse handleIllegalTokenException(IllegalTokenException ex) {
        log.info("illegalToken exception", ex);
        return new ExceptionResponse(ex.getExceptionCode());
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ExceptionResponse handleRefreshTokenExpiredException(RefreshTokenExpiredException ex) {
        log.info("refreshToken expired exception", ex);
        return new ExceptionResponse(ex.getExceptionCode());
    }
}
