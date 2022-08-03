package com.prgrms.team03linkbookbe.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.team03linkbookbe.common.exception.ExceptionCode;
import com.prgrms.team03linkbookbe.common.response.ExceptionResponse;
import com.prgrms.team03linkbookbe.jwt.exception.AccessTokenExpiredException;
import com.prgrms.team03linkbookbe.jwt.exception.IllegalTokenException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (TokenExpiredException e) {
            log.warn("AccessToken 유효기한 만료되었습니다. {}", e.getMessage());
            setErrorResponse(HttpStatus.FORBIDDEN, response, new AccessTokenExpiredException().getExceptionCode());
        } catch (JWTVerificationException e) {
            log.warn("Jwt processing failed: {}", e.getMessage());
            setErrorResponse(HttpStatus.FORBIDDEN, response, new IllegalTokenException().getExceptionCode());
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, ExceptionCode exceptionCode)
        throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setContentType("text/plain;charset=UTF-8");
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
            .exceptionCode(exceptionCode)
            .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(exceptionResponse);
        response.getWriter().write(json);
    }
}
