package com.prgrms.team03linkbookbe.folder.controller;

import com.prgrms.team03linkbookbe.common.exception.NoDataException;
import com.prgrms.team03linkbookbe.common.response.ExceptionResponse;
import com.prgrms.team03linkbookbe.folder.exception.IllegalAccessToPrivateFolderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice("com.prgrms.team03linkbookbe.folder")
public class FolderControllerAdvice {

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(IllegalAccessToPrivateFolderException.class)
    public ExceptionResponse handleIllegalAccessToPrivateFolderException(
        IllegalAccessToPrivateFolderException e) {
        log.info("private 폴더에 접근할 수 없습니다 : ", e);
        return new ExceptionResponse(e.getExceptionCode());
    }


}
