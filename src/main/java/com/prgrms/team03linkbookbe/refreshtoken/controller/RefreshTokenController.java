package com.prgrms.team03linkbookbe.refreshtoken.controller;


import com.prgrms.team03linkbookbe.config.JwtConfigure;
import com.prgrms.team03linkbookbe.refreshtoken.dto.AccessTokenResponseDto;
import com.prgrms.team03linkbookbe.refreshtoken.service.RefreshTokenService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/api/refresh-token")
    public AccessTokenResponseDto reissueAccessTokenByRefreshToken (@RequestHeader(value = "Access-Token") String accessToken, @RequestHeader(value = "Refresh-Token") String refreshToken) {
        return refreshTokenService.reissueAccessToken(accessToken, refreshToken);
    }

}
