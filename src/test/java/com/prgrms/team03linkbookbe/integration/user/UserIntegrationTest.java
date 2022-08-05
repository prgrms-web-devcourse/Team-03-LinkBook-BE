package com.prgrms.team03linkbookbe.integration.user;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.team03linkbookbe.annotation.WithJwtAuth;
import com.prgrms.team03linkbookbe.interest.dto.InterestDto;
import com.prgrms.team03linkbookbe.user.dto.LoginRequestDto;
import com.prgrms.team03linkbookbe.user.dto.RegisterRequestDto;
import com.prgrms.team03linkbookbe.user.dto.UserUpdateRequestDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    String email = "login1234@gmail.com";
    String password = "qwer1234!!";

    @Test
    @Order(1)
    @DisplayName("회원가입 테스트")
    void REGISTER_TEST() throws Exception {
        // given
        RegisterRequestDto requestDto = RegisterRequestDto.builder()
            .email(email)
            .password(password)
            .image("기본이미지URL")
            .build();

        // when & then
        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("user-register"));
    }

    @Test
    @Order(2)
    @DisplayName("로그인 테스트")
    void LOGIN_TEST() throws Exception {
        // given
        LoginRequestDto requestDto = LoginRequestDto.builder()
            .email(email)
            .password(password)
            .build();

        // when & then
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("user-login",
                responseFields(
                    fieldWithPath("accessToken").type(JsonFieldType.STRING)
                        .description("accessToken"),
                    fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                        .description("refreshToken"),
                    fieldWithPath("isFirstLogin").type(JsonFieldType.BOOLEAN)
                        .description("isFirstLogin"),
                    fieldWithPath("user").type(JsonFieldType.OBJECT)
                        .description("user"),
                    fieldWithPath("user.id").type(JsonFieldType.NUMBER)
                        .description("user.id"),
                    fieldWithPath("user.email").type(JsonFieldType.STRING)
                        .description("user.email"),
                    fieldWithPath("user.name").type(JsonFieldType.STRING)
                        .description("user.name"),
                    fieldWithPath("user.image").type(JsonFieldType.STRING)
                        .description("user.image"),
                    fieldWithPath("user.introduce").type(JsonFieldType.NULL)
                        .description("user.introduce (STRING, can be NULL)")
                )
            ));
    }

    @Test
    @Order(3)
    @DisplayName("개인정보 조회 테스트")
    @WithJwtAuth(email = "login1234@gmail.com")
    void ME_TEST() throws Exception {
        // given & when & then
        mockMvc.perform(get("/api/users/me"))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("user-me",
                responseFields(
                    fieldWithPath("user").type(JsonFieldType.OBJECT)
                        .description("user"),
                    fieldWithPath("user.id").type(JsonFieldType.NUMBER)
                        .description("user.id"),
                    fieldWithPath("user.email").type(JsonFieldType.STRING)
                        .description("user.email"),
                    fieldWithPath("user.name").type(JsonFieldType.STRING)
                        .description("user.name"),
                    fieldWithPath("user.image").type(JsonFieldType.STRING)
                        .description("user.image"),
                    fieldWithPath("user.introduce").type(JsonFieldType.NULL)
                        .description("user.introduce (STRING, can be NULL)"),
                    fieldWithPath("user.role").type(JsonFieldType.STRING)
                        .description("user.role"),
                    fieldWithPath("user.createdAt").type(JsonFieldType.STRING)
                        .description("user.createdAt"),
                    fieldWithPath("user.updatedAt").type(JsonFieldType.STRING)
                        .description("user.updatedAt"),
                    fieldWithPath("user.interests").type(JsonFieldType.ARRAY)
                        .description("user.interests")
                )
            ));
    }

    @Test
    @Order(4)
    @DisplayName("개인정보 수정 테스트")
    @WithJwtAuth(email = "login1234@gmail.com")
    void USER_UPDATE_TEST() throws Exception {
        // given
        List<InterestDto> interests = new ArrayList<>();
        interests.add(InterestDto.builder().field("흥미1").build());
        interests.add(InterestDto.builder().field("흥미2").build());

        UserUpdateRequestDto requestDto = UserUpdateRequestDto.builder()
            .name("변경할이름")
            .image("변경할이미지URL")
            .introduce("변경할소갯말")
            .interests(interests)
            .build();

        // when & then
        mockMvc.perform(patch("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("user-update"));
    }
}
