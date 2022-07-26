package com.prgrms.team03linkbookbe.common.exception;

import com.prgrms.team03linkbookbe.common.response.ExceptionResponse;
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
}
