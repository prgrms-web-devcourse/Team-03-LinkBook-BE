package com.prgrms.team03linkbookbe.email.exception;

import com.prgrms.team03linkbookbe.common.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class EmailCertificationFailureException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public EmailCertificationFailureException() {
        this.exceptionCode = ExceptionCode.EMAIL_CERTIFICATION_FAILURE;
    }

}
