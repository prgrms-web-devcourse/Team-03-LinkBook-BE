package com.prgrms.team03linkbookbe.unit.refreshtoken.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.prgrms.team03linkbookbe.config.WebSecurityConfigure;
import com.prgrms.team03linkbookbe.refreshtoken.controller.RefreshTokenController;
import com.prgrms.team03linkbookbe.refreshtoken.dto.AccessTokenResponseDto;
import com.prgrms.team03linkbookbe.refreshtoken.service.RefreshTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = RefreshTokenController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigure.class)
    }
)
public class RefreshTokenControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RefreshTokenService service;

    String refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IklsaHdhbiBMZWUiLCJpYXQiOjE1MTYyMzkwMjJ9.HjKjCVRYo5kZH1tDbFzh5HLYwEB6WTqdbFIQLTWLA6U";
    String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    String reissuedAccessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6InFxIExlZSIsImlhdCI6MTUxNjIzOTAyMn0.Fs9sMsN-DQzQwlrQYrSUy-PKpFRHHk6xxzbucYmxwpM";

    @Test
    @DisplayName("액세스 토큰 재발급 테스트")
    @WithMockUser
    void ACCESS_TOKEN_REISSUE_TEST() throws Exception {
        // given
        AccessTokenResponseDto responseDto = AccessTokenResponseDto.builder()
            .accessToken(reissuedAccessToken)
            .build();
        given(service.reissueAccessToken(accessToken, refreshToken)).willReturn(responseDto);

        // when & then
        mockMvc.perform(get("/api/refresh-token")
                .header("Access-Token", accessToken)
                .header("Refresh-Token", refreshToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value(reissuedAccessToken));
    }
}