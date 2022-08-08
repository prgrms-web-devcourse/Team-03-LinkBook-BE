package com.prgrms.team03linkbookbe.unit.email.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import com.prgrms.team03linkbookbe.email.dto.EmailCertificationRequestDto;
import com.prgrms.team03linkbookbe.email.dto.EmailRequestDto;
import com.prgrms.team03linkbookbe.email.exception.EmailCertificationFailureException;
import com.prgrms.team03linkbookbe.email.service.EmailService;
import java.util.Map;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockHttpSession;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @InjectMocks
    EmailService emailService;

    @Mock
    JavaMailSender mailSender;

    @Mock
    MimeMessage mimeMessage;

    MockHttpSession httpSession = new MockHttpSession();

    @DisplayName("sendEmail 메서드 : ")
    @Nested
    class SendEmail {
        String userEmail = "user@gmail.com";
        EmailRequestDto requestDto = EmailRequestDto.builder()
            .email(userEmail)
            .build();

        @Test
        @DisplayName("인증키 이메일 전송에 성공할 수 있다.")
        void SUCCESS_SEND_EMAIL_TEST() {
            // Given
            given(mailSender.createMimeMessage()).willReturn(mimeMessage);

            // When
            emailService.sendEmail(httpSession, requestDto);

            // Then
            Map value = (Map) httpSession.getAttribute(userEmail);
            Boolean isCertificated = (Boolean) value.get("IS_CERTIFICATION");
            assertThat(httpSession.getAttribute(userEmail)).isNotNull();
            assertThat(isCertificated).isFalse();
        }
    }

    @DisplayName("emailCertification 메서드 : ")
    @Nested
    class EmailCertification {
        String userEmail = "user@gmail.com";
        String userKey = "emailKey";
        EmailCertificationRequestDto requestDto = EmailCertificationRequestDto.builder()
            .email(userEmail)
            .key(userKey)
            .build();


        @Test
        @DisplayName("인증키와 세션키가 일치하여 인증에 성공한다.")
        void SUCCESS_EMAIL_CERTIFICATION_TEST() {
            // Given
            httpSession = new MockHttpSession();
            httpSession.setAttribute(userEmail, Map.of("CERTIFICATION_KEY", userKey, "IS_CERTIFICATION", false));

            // When
            emailService.emailCertification(httpSession, requestDto);

            // Then
            Map value = (Map) httpSession.getAttribute(userEmail);
            String key = (String) value.get("CERTIFICATION_KEY");
            Boolean isCertificated = (Boolean) value.get("IS_CERTIFICATION");
            assertThat(isCertificated).isTrue();
            assertThat(key).isEqualTo(requestDto.getKey());
        }

        @Test
        @DisplayName("인증키와 세션키가 일치하지 않으면 예외가 발생한다.")
        void FAIL_EMAIL_CERTIFICATION_TEST() {
            // Given
            httpSession = new MockHttpSession();
            httpSession.setAttribute(userEmail, Map.of("CERTIFICATION_KEY", "failKey", "IS_CERTIFICATION", false));

            // When Then
            assertThatThrownBy(() -> emailService.emailCertification(httpSession, requestDto))
                .isInstanceOf(EmailCertificationFailureException.class);
        }
    }

}
