package com.prgrms.team03linkbookbe.integration.refreshtoken;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.prgrms.team03linkbookbe.config.JwtConfigure;
import com.prgrms.team03linkbookbe.jwt.Jwt;
import com.prgrms.team03linkbookbe.jwt.Jwt.Claims;
import com.prgrms.team03linkbookbe.refreshtoken.dto.AccessTokenResponseDto;
import com.prgrms.team03linkbookbe.refreshtoken.entity.RefreshToken;
import com.prgrms.team03linkbookbe.refreshtoken.repository.RefreshTokenRepository;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import java.util.Calendar;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class RefreshTokenIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtConfigure jwtConfigure;

    @Autowired
    Jwt jwt;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Test
    @DisplayName("액세스 토큰 재발급 테스트")
    void REISSUE_ACCESS_TOKEN_TEST() throws Exception {
        // given
        String email = "example1@gmail.com";
        String[] roles = {"ROLE_USER"};

        // 액세스 토큰 재발급을 위해 유효기간 지난 것으로 생성
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2022);
        cal.set(Calendar.MONTH, Calendar.AUGUST);
        cal.set(Calendar.DAY_OF_MONTH, 3);
        Date date = cal.getTime();
        JWTCreator.Builder builder = com.auth0.jwt.JWT.create();
        builder.withIssuer(jwtConfigure.getClientSecret());
        builder.withIssuedAt(date);
        builder.withExpiresAt(
            new Date(date.getTime() + jwtConfigure.getAccessTokenExpirySeconds() * 1_000L));
        builder.withClaim("email", email);
        builder.withArrayClaim("roles", roles);
        String accessToken = builder.sign(Algorithm.HMAC512(jwtConfigure.getClientSecret()));

        Claims claims = Claims.from(email, roles);

        String refreshToken = jwt.createRefreshToken(claims);

        User newUser = User.builder()
            .email(email)
            .password("비밀번호")
            .image("이미지URL")
            .name("닉네임")
            .role("ROLE_USER")
            .build();

        User user = userRepository.save(newUser);

        RefreshToken refreshTokenEntity = RefreshToken.builder()
            .user(user)
            .token(refreshToken)
            .build();

        refreshTokenRepository.save(refreshTokenEntity);

        AccessTokenResponseDto responseDto = AccessTokenResponseDto.fromEntity(accessToken, user);

        // when & then
        mockMvc.perform(get("/api/refresh-token")
                .header("Access-Token", accessToken)
                .header("Refresh-Token", refreshToken))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("reissue-access-token",
                requestHeaders(
                    headerWithName("Access-Token").description("accessToken"),
                    headerWithName("Refresh-Token").description("refreshToken")
                ),
                responseFields(
                    fieldWithPath("accessToken").type(JsonFieldType.STRING)
                        .description("accessToken"),
                    fieldWithPath("userDetail").type(JsonFieldType.OBJECT)
                        .description("userDetail"),
                    fieldWithPath("userDetail.id").type(JsonFieldType.NUMBER)
                        .description("userDetail.id"),
                    fieldWithPath("userDetail.email").type(JsonFieldType.STRING)
                        .description("userDetail.email"),
                    fieldWithPath("userDetail.name").type(JsonFieldType.STRING)
                        .description("userDetail.name"),
                    fieldWithPath("userDetail.image").type(JsonFieldType.STRING)
                        .description("userDetail.image")
                )
            ));
    }
}
