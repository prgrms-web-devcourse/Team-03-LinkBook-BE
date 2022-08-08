package com.prgrms.team03linkbookbe.email.controller;

import com.prgrms.team03linkbookbe.email.dto.EmailCertificationRequestDto;
import com.prgrms.team03linkbookbe.email.dto.EmailRequestDto;
import com.prgrms.team03linkbookbe.email.service.EmailService;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/api/emails")
    public ResponseEntity<Void> sendEmail(HttpServletRequest request, @RequestBody EmailRequestDto requestDto) {
        emailService.sendEmail(request.getSession(), requestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/emails/certification")
    public ResponseEntity<Void> emailCertification(HttpServletRequest request, @RequestBody EmailCertificationRequestDto requestDto) {
        emailService.emailCertification(request.getSession(), requestDto);
        return ResponseEntity.ok().build();
    }

}
