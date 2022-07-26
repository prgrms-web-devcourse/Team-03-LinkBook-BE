package com.prgrms.team03linkbookbe.common.response;

import com.prgrms.team03linkbookbe.common.exception.ExceptionCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ExceptionResponse {

    private int code;
    private String message;

    @Builder
    public ExceptionResponse(ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMsg();
    }

}
