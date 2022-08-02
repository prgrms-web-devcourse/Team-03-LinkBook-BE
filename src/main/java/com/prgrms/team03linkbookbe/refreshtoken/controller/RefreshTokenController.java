package com.prgrms.team03linkbookbe.refreshtoken.controller;


import com.prgrms.team03linkbookbe.refreshtoken.dto.AccessTokenResponseDto;
import com.prgrms.team03linkbookbe.refreshtoken.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @GetMapping("/api/refresh-token")
    public ResponseEntity<AccessTokenResponseDto> reissueAccessTokenByRefreshToken(
        @RequestHeader(value = "Access-Token") String accessToken,
        @RequestHeader(value = "Refresh-Token") String refreshToken
    ) {
        AccessTokenResponseDto responseDto =
            refreshTokenService.reissueAccessToken(accessToken, refreshToken);
        return ResponseEntity.ok().body(responseDto);
    }
}
