package com.prgrms.team03linkbookbe.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.team03linkbookbe.common.response.ExceptionResponse;
import com.prgrms.team03linkbookbe.jwt.ExceptionHandlerFilter;
import com.prgrms.team03linkbookbe.jwt.Jwt;
import com.prgrms.team03linkbookbe.jwt.JwtAuthenticationFilter;
import com.prgrms.team03linkbookbe.jwt.JwtAuthenticationProvider;
import com.prgrms.team03linkbookbe.jwt.exception.AccessDeniedException;
import com.prgrms.team03linkbookbe.refreshtoken.service.RefreshTokenService;
import com.prgrms.team03linkbookbe.user.service.UserService;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfigure {

    private final JwtConfigure jwtConfigure;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Jwt jwt() {
        return new Jwt(
            jwtConfigure.getIssuer(),
            jwtConfigure.getClientSecret(),
            jwtConfigure.getAccessTokenExpirySeconds(),
            jwtConfigure.getRefreshTokenExpirySeconds()
        );
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(Jwt jwt, UserService userService, RefreshTokenService refreshTokenService) {
        return new JwtAuthenticationProvider(jwt, userService, refreshTokenService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    public JwtAuthenticationFilter jwtAuthenticationFilter(Jwt jwt) {
        return new JwtAuthenticationFilter(jwtConfigure.getAccessHeader(), jwtConfigure.getRefreshHeader(), jwt);
    }

    public ExceptionHandlerFilter exceptionHandlerFilter() {
        return new ExceptionHandlerFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, Jwt jwt) throws Exception {
        return http
            .authorizeRequests()
            .antMatchers("/api/users/login", "/api/users/register", "/h2-console/**", "/api/refresh-token").permitAll()
            .anyRequest().hasAnyRole("USER")
            .and()
            /**
             * formLogin, csrf, headers, http-basic, rememberMe, logout filter 비활성화
             */
            .formLogin()
            .disable()
            .csrf()
            .disable()
            .headers()
            .disable()
            .httpBasic()
            .disable()
            .rememberMe()
            .disable()
            .logout()
            .disable()
            /**
             * Session 사용하지 않음
             */
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            /**
             * 예외처리 핸들러
             */
            .exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler())
            .and()
            /**
             * JwtAuthenticationFilter FilterChain 추가
             */
            .addFilterBefore(jwtAuthenticationFilter(jwt), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(exceptionHandlerFilter(), JwtAuthenticationFilter.class)
            .build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, e) -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication != null ? authentication.getPrincipal() : null;
            log.warn("{} is denied", principal, e);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("text/plain;charset=UTF-8");
            ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .exceptionCode(new AccessDeniedException().getExceptionCode())
                .build();
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(exceptionResponse);
            response.getWriter().write(json);
            response.getWriter().flush();
            response.getWriter().close();
        };
    }

}


