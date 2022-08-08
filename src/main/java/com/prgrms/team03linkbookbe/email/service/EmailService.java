package com.prgrms.team03linkbookbe.email.service;

import com.prgrms.team03linkbookbe.email.dto.EmailCertificationRequestDto;
import com.prgrms.team03linkbookbe.email.dto.EmailRequestDto;
import com.prgrms.team03linkbookbe.email.exception.EmailCertificationFailureException;
import com.prgrms.team03linkbookbe.email.exception.EmailIsNotCertificatedException;
import com.prgrms.team03linkbookbe.email.exception.EmailSendFailureException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private static final String CERTIFICATION_KEY = "CERTIFICATION_KEY";
    private static final String IS_CERTIFICATION = "IS_CERTIFICATION";

    public void sendEmail(HttpSession httpSession, EmailRequestDto requestDto) {
        try {

            MimeMessage mimeMessage = emailSender.createMimeMessage();
            String userEmail = requestDto.getEmail();
            String key = createKey();
            String message = createMessage(userEmail, key);

            httpSession.setAttribute(userEmail, Map.of(CERTIFICATION_KEY, key, IS_CERTIFICATION, false));
            httpSession.setMaxInactiveInterval(60 * 30);

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setTo(userEmail);
            helper.setSubject("회원가입 이메일 인증");
            helper.setText(message, true);
            emailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new EmailSendFailureException();
        }
    }

    public void emailCertification(HttpSession httpSession, EmailCertificationRequestDto requestDto) {
        String userEmail = requestDto.getEmail();
        String userKey = requestDto.getKey();

        Map value = (Map) httpSession.getAttribute(userEmail);
        String key = (String) value.get(CERTIFICATION_KEY);
        if(!key.equals(userKey)) {
            throw new EmailCertificationFailureException();
        }
        httpSession.setAttribute(userEmail, Map.of(CERTIFICATION_KEY, key, IS_CERTIFICATION, true));
        log.info("이메일 인증 성공");
    }

    public void IsCertificatedEmail(HttpSession httpSession, String userEmail) {
        Map value = (Map) httpSession.getAttribute(userEmail);
        if(value == null) {
            throw new EmailIsNotCertificatedException();
        }
        String key = (String) value.get(CERTIFICATION_KEY);
        Boolean isCertification = (Boolean) value.get(IS_CERTIFICATION);
        if(!isCertification) {
            throw new EmailIsNotCertificatedException();
        }
    }

    private String createMessage(String userEmail, String key) {
        log.info("userEmail : {}", userEmail);
        log.info("certificationNumber : {}", key);

        String message="";
        message+= "<div style='margin:100px;'>";
        message+= "<h1> 안녕하세요 LinkBook입니다. </h1>";
        message+= "<br>";
        message+= "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
        message+= "<br>";
        message+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        message+= "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        message+= "<div style='font-size:130%'>";
        message+= "CODE : <strong>";
        message+= key+"</strong><div><br/> ";
        message+= "</div>";

        return message;
    }

    private String createKey() {
        StringBuilder key = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0: //  a~z
                    key.append((char) ((int) (random.nextInt(26)) + 97));
                    break;
                case 1: //  A~Z
                    key.append((char) ((int) (random.nextInt(26)) + 65));

                    break;
                case 2: // 0~9
                    key.append((random.nextInt(10)));
                    break;
            }
        }

        return key.toString();
    }



}
