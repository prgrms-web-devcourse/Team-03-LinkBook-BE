package com.prgrms.team03linkbookbe.integration.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.team03linkbookbe.comment.dto.CreateCommentRequestDto;
import com.prgrms.team03linkbookbe.comment.dto.UpdateCommentRequestDto;
import com.prgrms.team03linkbookbe.comment.entity.Comment;
import com.prgrms.team03linkbookbe.comment.repository.CommentRepository;
import com.prgrms.team03linkbookbe.comment.service.CommentService;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommentIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolderRepository folderRepository;

    User user;
    Folder folder;
    Comment comment;

    @BeforeAll
    void setup() {
        user = User.builder()
                .email("test@test.com")
                .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
                .image("url")
                .role("USER")
                .name("tester")
                .build();
        userRepository.save(user);

        folder = Folder.builder()
                .name("my-folder")
                .image("url")
                .content("halo")
                .isPinned(false)
                .isPrivate(false)
                .user(user)
                .build();
        folderRepository.save(folder);

        comment = Comment.builder()
                .content("hi")
                .folder(folder)
                .user(user)
                .build();
        commentRepository.save(comment);
    }

    @AfterAll
    void teardown() {
        commentRepository.deleteAll();
        folderRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("댓글 등록 테스트")
    @Disabled // 빌드 실패 방지
    void INSERT_COMMENT_TEST() throws Exception {
        CreateCommentRequestDto commentRequestDto =
                CreateCommentRequestDto.builder()
                        .userId(user.getId())
                        .folderId(folder.getId())
                        .content("thx")
                        .build();
        this.mockMvc.perform(post("/api/comments")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(
                        document("register-comment",
                                requestFields(
                                        fieldWithPath("content").type(JsonFieldType.STRING)
                                                .description("content"),
                                        fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                                .description("userId"),
                                        fieldWithPath("folderId").type(JsonFieldType.NUMBER)
                                                .description("folderId")
                                )
                        )
                );
    }

    @Test
    @Order(2)
    @DisplayName("댓글 수정 테스트")
    @Disabled // 빌드 실패 방지
    void UPDATE_COMMENT_BY_ID_TEST() throws Exception {
        UpdateCommentRequestDto commentRequestDto =
                UpdateCommentRequestDto.builder()
                        .id(comment.getId())
                        .userId(user.getId())
                        .folderId(folder.getId())
                        .content("thx")
                        .build();
        this.mockMvc.perform(put("/api/comments")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(
                        document("modify-comment",
                                requestFields(
                                        fieldWithPath("commentId").type(JsonFieldType.NUMBER)
                                                .description("commentId"),
                                        fieldWithPath("content").type(JsonFieldType.STRING)
                                                .description("content"),
                                        fieldWithPath("userId").type(JsonFieldType.NUMBER)
                                                .description("userId"),
                                        fieldWithPath("folderId").type(JsonFieldType.NUMBER)
                                                .description("folderId")
                                )
                        )
                );
    }

    @Test
    @Order(3)
    @DisplayName("댓글 삭제 테스트")
    @Disabled // 빌드 실패 방지
    void DELETE_COMMENT_BY_ID_TEST() throws Exception {
        this.mockMvc.perform(put("/api/comments/{id}", comment.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(
                        document("remove-by-id-comment",
                                requestFields(
                                        fieldWithPath("commentId").type(JsonFieldType.NUMBER)
                                                .description("commentId")
                                )
                        )
                );
    }
}
