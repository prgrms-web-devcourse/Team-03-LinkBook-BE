package com.prgrms.team03linkbookbe.integration.email;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.team03linkbookbe.email.dto.EmailCertificationRequestDto;
import com.prgrms.team03linkbookbe.email.dto.EmailRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class EmailIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("사용자 이메일을 받아서 인증 이메일 전송 할 수 있다.")
    void SEND_EMAIL_TEST() throws Exception {
        // Given
        String userEmail = "user@gmail.com";
        EmailRequestDto requestDto = EmailRequestDto.builder()
            .email(userEmail)
            .build();
        MockHttpSession mockHttpSession = new MockHttpSession();

        // When
        ResultActions perform = mockMvc.perform(post("/api/emails")
            .session(mockHttpSession)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))
            .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // Then
        perform.andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());

        // Documentation
        perform.andDo(document("email-send"));
    }

    @Test
    @DisplayName("인증 이메일 전송에 실패 할 수 있다.")
    void FAILURE_EMAIL_SEND_TEST() throws Exception {
        // Given
        EmailRequestDto requestDto = EmailRequestDto.builder()
            .build();
        MockHttpSession mockHttpSession = new MockHttpSession();

        // When
        ResultActions perform = mockMvc.perform(post("/api/emails")
            .session(mockHttpSession)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))
            .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // Then
        perform.andExpect(status().isBadRequest())
            .andDo(MockMvcResultHandlers.print());

        // Documentation
        perform.andDo(document("email-send",
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER).description("errorCode"),
                fieldWithPath("message").type(JsonFieldType.STRING).description("errorMessage")
            )
        ));
    }

    @Test
    @DisplayName("이메일 인증 번호 검증에 성공할 수 있다.")
    void SUCCESS_EMAIL_CERTIFICATION_TEST() throws Exception {
        // Given
        String userEmail = "user@gmail.com";
        String userKey = "emailKey";
        EmailCertificationRequestDto requestDto = EmailCertificationRequestDto.builder()
            .email(userEmail)
            .key(userKey)
            .build();
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute(userEmail, userKey);

        // When
        ResultActions perform = mockMvc.perform(post("/api/emails/certification")
            .session(mockHttpSession)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))
            .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // Then
        perform.andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());

        // Documentation
        perform.andDo(document("email-certification"));
    }

    @Test
    @DisplayName("이메일 인증 번호 검증에 실패할 수 있다.")
    void FAILURE_EMAIL_CER() throws Exception {
        // Given
        String userEmail = "user@gmail.com";
        String userKey = "emailKey";
        EmailCertificationRequestDto requestDto = EmailCertificationRequestDto.builder()
            .email(userEmail)
            .key(userKey)
            .build();
        MockHttpSession mockHttpSession = new MockHttpSession();
        mockHttpSession.setAttribute(userEmail, "emailKeyFailure");

        // When
        ResultActions perform = mockMvc.perform(post("/api/emails/certification")
            .session(mockHttpSession)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))
            .with(SecurityMockMvcRequestPostProcessors.csrf()));

        // Then
        perform.andExpect(status().isBadRequest())
            .andDo(MockMvcResultHandlers.print());

        // Documentation
        perform.andDo(document("email-certification",
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER).description("errorCode"),
                fieldWithPath("message").type(JsonFieldType.STRING).description("errorMessage")
            )
        ));
    }

}
