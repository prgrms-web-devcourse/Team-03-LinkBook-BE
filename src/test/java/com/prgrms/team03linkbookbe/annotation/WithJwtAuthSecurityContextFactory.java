package com.prgrms.team03linkbookbe.annotation;

import com.prgrms.team03linkbookbe.jwt.JwtAuthentication;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithJwtAuthSecurityContextFactory implements WithSecurityContextFactory<WithJwtAuth> {

    @Override
    public SecurityContext createSecurityContext(WithJwtAuth annotation) {
        String email = annotation.email();

        JwtAuthentication authentication =
            new JwtAuthentication("access-token", "refresh-token", email);
        UsernamePasswordAuthenticationToken token =
            new UsernamePasswordAuthenticationToken(authentication, "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
        return context;
    }
}