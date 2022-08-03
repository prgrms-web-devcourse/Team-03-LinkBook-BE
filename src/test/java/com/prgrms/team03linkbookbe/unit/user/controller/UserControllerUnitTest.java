package com.prgrms.team03linkbookbe.unit.user.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.team03linkbookbe.annotation.WithJwtAuth;
import com.prgrms.team03linkbookbe.config.WebSecurityConfigure;
import com.prgrms.team03linkbookbe.jwt.JwtAuthentication;
import com.prgrms.team03linkbookbe.jwt.JwtAuthenticationToken;
import com.prgrms.team03linkbookbe.user.controller.UserController;
import com.prgrms.team03linkbookbe.user.dto.LoginRequestDto;
import com.prgrms.team03linkbookbe.user.dto.RegisterRequestDto;
import com.prgrms.team03linkbookbe.user.dto.UserResponseDto;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = UserController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigure.class)
    }
)
public class UserControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService service;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("회원가입 테스트")
    @WithMockUser
    void REGISTER_TEST() throws Exception {
        // given
        RegisterRequestDto requestDto = RegisterRequestDto.builder()
            .email("example1@gmail.com")
            .password("qwer1234!!")
            .build();
        doNothing().when(service).register(requestDto);

        // when & then
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 테스트")
    void LOGIN_TEST() throws Exception {
        // given
        String email = "example1@gmail.com";
        String password = "qwer1234!!";

        User user = User.builder()
            .build();

        LoginRequestDto requestDto = LoginRequestDto.builder()
            .email(email)
            .password(password)
            .build();

        given(service.login(email, password)).willReturn(user);

        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(email, password);
        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        String refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IklsaHdhbiBMZWUiLCJpYXQiOjE1MTYyMzkwMjJ9.HjKjCVRYo5kZH1tDbFzh5HLYwEB6WTqdbFIQLTWLA6U";
        JwtAuthentication jwtAuthentication =
            new JwtAuthentication(accessToken, refreshToken, email);
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_USER"));
        JwtAuthenticationToken authenticated =
            new JwtAuthenticationToken(jwtAuthentication, password, roles);

        given(authenticationManager.authenticate(jwtAuthenticationToken)).willReturn(
            authenticated);

        // when & then
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value(accessToken))
            .andExpect(jsonPath("$.refreshToken").value(refreshToken));
    }

    @Test
    @DisplayName("개인정보 조회 테스트")
    @WithJwtAuth(email = "example1@gmail.com")
    void ME_TEST() throws Exception {
        // given
        String email = "example1@gmail.com";
        UserResponseDto responseDto = UserResponseDto.builder()
            .email(email)
            .image("사용자 이미지 URL")
            .name("닉네임")
            .role("ROLE_USER")
            .build();

        given(service.findByEmail(email)).willReturn(responseDto);

        // when & then
        mockMvc.perform(get("/api/users/me")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
            .andExpect(status().isOk());
    }
}
