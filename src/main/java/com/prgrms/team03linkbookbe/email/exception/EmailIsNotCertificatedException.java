package com.prgrms.team03linkbookbe.email.exception;

import com.prgrms.team03linkbookbe.common.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class EmailIsNotCertificatedException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public EmailIsNotCertificatedException() {
        this.exceptionCode = ExceptionCode.EMAIL_IS_NOT_CERTIFICATED;
    }

}
