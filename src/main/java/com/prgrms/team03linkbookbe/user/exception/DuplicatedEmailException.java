package com.prgrms.team03linkbookbe.user.exception;

import com.prgrms.team03linkbookbe.common.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class DuplicatedEmailException extends RuntimeException{

    private final ExceptionCode exceptionCode;

    public DuplicatedEmailException() {
        this.exceptionCode = ExceptionCode.DUPLICATED_EMAIL;
    }
}
