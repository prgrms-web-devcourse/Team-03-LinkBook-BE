package com.prgrms.team03linkbookbe.user.controller;

import com.prgrms.team03linkbookbe.jwt.JwtAuthentication;
import com.prgrms.team03linkbookbe.jwt.JwtAuthenticationToken;
import com.prgrms.team03linkbookbe.user.dto.LoginRequestDto;
import com.prgrms.team03linkbookbe.user.dto.LoginResponseDto;
import com.prgrms.team03linkbookbe.user.dto.RegisterRequestDto;
import com.prgrms.team03linkbookbe.user.dto.RegisterResponseDto;
import com.prgrms.team03linkbookbe.user.dto.UpdateRequestDto;
import com.prgrms.team03linkbookbe.user.dto.UserResponseDto;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<UserResponseDto> me(@AuthenticationPrincipal JwtAuthentication authentication) {
        UserResponseDto responseDto = userService.findByEmail(authentication.email);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/api/users/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(
            request.getEmail(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(jwtAuthenticationToken);
        JwtAuthenticationToken authenticated = (JwtAuthenticationToken) authentication;
        JwtAuthentication principal = (JwtAuthentication) authenticated.getPrincipal();
        LoginResponseDto responseDto = LoginResponseDto.builder()
            .accessToken(principal.accessToken)
            .refreshToken(principal.refreshToken)
            .build();
        return ResponseEntity.ok().body(responseDto);
    }

    @PostMapping("/api/users/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequestDto requestDto) {
        userService.register(requestDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/api/users")
    public ResponseEntity<Void> update(@RequestBody UpdateRequestDto requestDto, @AuthenticationPrincipal JwtAuthentication authentication) {
        userService.updateUser(requestDto, authentication.email);
        return ResponseEntity.ok().build();
    }

}
