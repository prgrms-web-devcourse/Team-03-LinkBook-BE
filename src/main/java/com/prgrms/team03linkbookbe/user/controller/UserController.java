package com.prgrms.team03linkbookbe.user.controller;

import com.prgrms.team03linkbookbe.jwt.JwtAuthentication;
import com.prgrms.team03linkbookbe.jwt.JwtAuthenticationToken;
import com.prgrms.team03linkbookbe.user.dto.LoginRequestDto;
import com.prgrms.team03linkbookbe.user.dto.LoginResponseDto;
import com.prgrms.team03linkbookbe.user.dto.MeResponseDto;
import com.prgrms.team03linkbookbe.user.dto.RegisterRequestDto;
import com.prgrms.team03linkbookbe.user.dto.UserUpdateRequestDto;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    @GetMapping("/api/users/me")
    public ResponseEntity<MeResponseDto> me(
        @AuthenticationPrincipal JwtAuthentication authentication) {
        MeResponseDto responseDto = userService.me(authentication.email);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/api/users/login")
    public ResponseEntity<LoginResponseDto> login(@Validated @RequestBody LoginRequestDto request) {
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(
            request.getEmail(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(jwtAuthenticationToken);
        JwtAuthenticationToken authenticated = (JwtAuthenticationToken) authentication;
        JwtAuthentication principal = (JwtAuthentication) authenticated.getPrincipal();
        User user = (User) authenticated.getDetails();
        Boolean isFirstLogin = Boolean.FALSE;
        if (user.getLastLoginAt() == null) {
            isFirstLogin = Boolean.TRUE;
        }
        userService.updateLastLoginAt(user);
        LoginResponseDto responseDto = LoginResponseDto.fromEntity(principal.accessToken,
            principal.refreshToken, user, isFirstLogin);
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/api/users/register")
    public ResponseEntity<Void> register(HttpServletRequest request,
        @Validated @RequestBody RegisterRequestDto requestDto) {
        HttpSession httpSession = request.getSession();
        userService.register(httpSession, requestDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/api/users")
    public ResponseEntity<Void> update(@RequestBody UserUpdateRequestDto requestDto,
        @AuthenticationPrincipal JwtAuthentication authentication) {
        log.info("{}", authentication.email);
        userService.updateUser(requestDto, authentication.email);
        return ResponseEntity.ok().build();
    }
}
