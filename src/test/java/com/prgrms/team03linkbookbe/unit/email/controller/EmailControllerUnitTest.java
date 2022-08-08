package com.prgrms.team03linkbookbe.unit.email.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.team03linkbookbe.email.controller.EmailController;
import com.prgrms.team03linkbookbe.email.dto.EmailCertificationRequestDto;
import com.prgrms.team03linkbookbe.email.dto.EmailRequestDto;
import com.prgrms.team03linkbookbe.email.exception.EmailCertificationFailureException;
import com.prgrms.team03linkbookbe.email.exception.EmailSendFailureException;
import com.prgrms.team03linkbookbe.email.service.EmailService;
import com.prgrms.team03linkbookbe.user.service.UserService;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest( {
    EmailController.class
})
public class EmailControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmailService emailService;

    @MockBean
    private UserService userService;

    HttpSession httpSession;

    @DisplayName("sendEmail 메서드 : ")
    @Nested
    class SendEmail {
        String userEmail = "user@gmail.com";
        EmailRequestDto requestDto = EmailRequestDto.builder()
            .email(userEmail)
            .build();


        @Test
        @DisplayName("인증 이메일 전송을 할 수 있다.")
        @WithMockUser
        void SEND_EMAIL_TEST() throws Exception {
            // Given
            httpSession = new MockHttpSession();
            willDoNothing().given(emailService).sendEmail(httpSession, requestDto);


            // When
            ResultActions perform = mockMvc.perform(post("/api/emails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

            // Then
            perform.andExpect(status().isOk());
        }

        @Test
        @DisplayName("인증 이메일 전송에 실패 할 수 있다.")
        @WithMockUser
        void FAILURE_EMAIL_SEND_TEST() throws Exception {
            // Given
            httpSession = new MockHttpSession();
            willThrow(new EmailSendFailureException()).given(emailService).sendEmail(any(HttpSession.class), any(EmailRequestDto.class));

            // When
            ResultActions perform = mockMvc.perform(post("/api/emails")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

            // Then
            perform.andExpect(status().isBadRequest());

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
        @DisplayName("이메일 인증 번호 검증에 성공할 수 있다.")
        @WithMockUser
        void SUCCESS_EMAIL_CERTIFICATION_TEST() throws Exception {
            // Given
            httpSession = new MockHttpSession();
            httpSession.setAttribute(userEmail, Map.of("KEY_NAME", "failKey", "IS_CERTIFICATION", false));
            willDoNothing().given(emailService).emailCertification(httpSession, requestDto);

            // When
            ResultActions perform = mockMvc.perform(post("/api/emails/certification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

            // Then
            perform.andExpect(status().isOk());
        }

        @Test
        @DisplayName("이메일 인증 번호 검증에 실패할 수 있다.")
        @WithMockUser
        void FAILURE_EMAIL_CER() throws Exception {
            // Given
            willThrow(new EmailCertificationFailureException()).given(emailService).emailCertification(any(HttpSession.class), any(EmailCertificationRequestDto.class));

            // When
            ResultActions perform = mockMvc.perform(post("/api/emails/certification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

            // Then
            perform.andExpect(status().isBadRequest());
        }
    }

}
