package com.prgrms.team03linkbookbe.email.exception;

import com.prgrms.team03linkbookbe.common.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class EmailSendFailureException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public EmailSendFailureException() {
        this.exceptionCode = ExceptionCode.EMAIL_SEND_FAILURE;
    }

}
