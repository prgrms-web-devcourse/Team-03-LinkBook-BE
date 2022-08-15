package com.prgrms.team03linkbookbe.folder.exception;

import com.prgrms.team03linkbookbe.common.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class IllegalAccessToPrivateFolderException extends RuntimeException{

    private final ExceptionCode exceptionCode;

    public IllegalAccessToPrivateFolderException() {
        this.exceptionCode = ExceptionCode.ILLEGAL_ACCESS_TO_PRIVATE_FOLDER;
    }

}
