package com.prgrms.team03linkbookbe.refreshtoken.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.prgrms.team03linkbookbe.common.exception.NoDataException;
import com.prgrms.team03linkbookbe.jwt.Jwt;
import com.prgrms.team03linkbookbe.jwt.Jwt.Claims;
import com.prgrms.team03linkbookbe.jwt.exception.IllegalTokenException;
import com.prgrms.team03linkbookbe.jwt.exception.RefreshTokenExpiredException;
import com.prgrms.team03linkbookbe.refreshtoken.dto.AccessTokenResponseDto;
import com.prgrms.team03linkbookbe.refreshtoken.entity.RefreshToken;
import com.prgrms.team03linkbookbe.refreshtoken.repository.RefreshTokenRepository;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final Jwt jwt;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public AccessTokenResponseDto reissueAccessToken(String accessToken, String refreshToken) {

        DecodedJWT decodedJWT = com.auth0.jwt.JWT.decode(accessToken);
        if (decodedJWT.getExpiresAt().after(new Date())) {
            log.info("AccessToken not Expired, AccessToken reissue denied");
            throw new IllegalTokenException();
        }

        try {
            Claims claims = jwt.verify(refreshToken);

            User user = userRepository.findByEmail(claims.getEmail())
                .orElseThrow(() -> new IllegalTokenException());

            RefreshToken findRefreshToken = refreshTokenRepository.findByUserId(user.getId())
                .orElseThrow(() -> new IllegalTokenException());

            if (!findRefreshToken.getToken().equals(refreshToken)) {
                throw new IllegalTokenException();
            }

            String newAccessToken = jwt.createAccessToken(claims);
            return AccessTokenResponseDto.fromEntity(newAccessToken, user);
        } catch (TokenExpiredException e) {
            throw new RefreshTokenExpiredException();
        } catch (JWTVerificationException e) {
            log.warn("Jwt processing failed: ", e);
            throw new IllegalTokenException();
        }

    }

    @Transactional
    public String issueRefreshToken(Claims claims) {
        String newRefreshToken = jwt.createRefreshToken(claims);
        User user = userRepository.findByEmail(claims.getEmail())
            .orElseThrow(() -> new NoDataException());

        refreshTokenRepository.findByUserId(user.getId())
            .ifPresentOrElse(
                refreshToken -> {
                    refreshToken.changeToken(newRefreshToken);
                    log.info("refreshToken update, email : {}", user.getEmail());
                },
                () -> {
                    refreshTokenRepository.save(RefreshToken.builder()
                            .token(newRefreshToken)
                            .user(user)
                        .build());
                    log.info("refreshToken create, email : {}", user.getEmail());
                });
        return newRefreshToken;
    }
}
