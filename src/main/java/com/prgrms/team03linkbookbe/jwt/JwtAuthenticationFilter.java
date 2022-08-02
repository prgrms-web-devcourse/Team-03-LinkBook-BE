package com.prgrms.team03linkbookbe.jwt;

import static org.apache.commons.lang3.StringUtils.*;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.prgrms.team03linkbookbe.jwt.exception.AccessTokenExpiredException;
import com.prgrms.team03linkbookbe.jwt.exception.IllegalTokenException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String accessHeaderKey;

    private final String refreshHeaderKey;

    private final Jwt jwt;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<String> maybeAccessToken = getAccessToken(request);
            Optional<String> maybeRefreshToken = getRefreshToken(request);
            if (maybeAccessToken.isPresent() && maybeRefreshToken.isEmpty()) {
                String accessToken = maybeAccessToken.get();
                Jwt.Claims claims = verify(accessToken);
                log.debug("Jwt parse result: {}", claims);

                String email = claims.getEmail();
                List<GrantedAuthority> authorities = getAuthorities(claims);

                if (isNotEmpty(email) && authorities.size() > 0) {
                    JwtAuthenticationToken authentication = new JwtAuthenticationToken(
                        new JwtAuthentication(accessToken, null, email), null, authorities);

                    authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } else {
            log.debug(
                "SecurityContextHolder not populated with security token, as it already contained: {}",
                SecurityContextHolder.getContext().getAuthentication()
            );
        }

        chain.doFilter(request, response);
    }

    private Optional<String> getAccessToken(HttpServletRequest request) {
        String token = request.getHeader(accessHeaderKey);
        if (isNotBlank(token)) {
            log.debug("Jwt token detected: {}");
            try {
                return Optional.of(URLDecoder.decode(token, "UTF-8"));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return Optional.empty();
    }

    private Optional<String> getRefreshToken(HttpServletRequest request) {
        String token = request.getHeader(refreshHeaderKey);
        if (isNotBlank(token)) {
            log.debug("Jwt refreshtoken detected: {}");
            try {
                return Optional.of(URLDecoder.decode(token, "UTF-8"));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return Optional.empty();
    }

    private Jwt.Claims verify(String token) {
        return jwt.verify(token);
    }

    private List<GrantedAuthority> getAuthorities(Jwt.Claims claims) {
        String[] roles = claims.getRoles();
        return roles == null || roles.length == 0
            ? Collections.emptyList() :
            Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(
                Collectors.toList());
    }
}
