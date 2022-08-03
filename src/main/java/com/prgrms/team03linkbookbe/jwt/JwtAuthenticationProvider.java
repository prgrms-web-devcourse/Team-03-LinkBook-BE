package com.prgrms.team03linkbookbe.jwt;

import com.prgrms.team03linkbookbe.refreshtoken.service.RefreshTokenService;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final Jwt jwt;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;

        return processUserAuthentication(
            String.valueOf(jwtAuthenticationToken.getPrincipal()),
            jwtAuthenticationToken.getCredentials()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private Authentication processUserAuthentication(String principal, String credentials) {
        try {
            User user = userService.login(principal, credentials);
            List<GrantedAuthority> authorities = user.getAuthorities();
            String accessToken = getAccessToken(user.getEmail(), authorities);
            String refreshToken = getRefreshToken(user.getEmail(), authorities);
            JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(
                new JwtAuthentication(accessToken, refreshToken, user.getEmail()), null, authorities);
            authenticationToken.setDetails(user);
            return authenticationToken;
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    private String getAccessToken(String email, List<GrantedAuthority> authorities) {
        String[] roles = authorities.stream().map(GrantedAuthority::getAuthority)
            .toArray(String[]::new);
        return jwt.createAccessToken(Jwt.Claims.from(email, roles));
    }

    private String getRefreshToken(String email, List<GrantedAuthority> authorities) {
        String[] roles = authorities.stream().map(GrantedAuthority::getAuthority)
            .toArray(String[]::new);
        return refreshTokenService.issueRefreshToken(Jwt.Claims.from(email, roles));
    }


}
