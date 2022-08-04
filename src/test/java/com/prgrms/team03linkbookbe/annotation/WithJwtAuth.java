package com.prgrms.team03linkbookbe.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithJwtAuthSecurityContextFactory.class)
public @interface WithJwtAuth {
    String email();
}