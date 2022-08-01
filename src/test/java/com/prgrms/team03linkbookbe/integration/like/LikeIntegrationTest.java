package com.prgrms.team03linkbookbe.integration.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.team03linkbookbe.comment.dto.CreateCommentRequestDto;
import com.prgrms.team03linkbookbe.comment.entity.Comment;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import com.prgrms.team03linkbookbe.like.dto.CreateLikeRequestDto;
import com.prgrms.team03linkbookbe.like.entity.Like;
import com.prgrms.team03linkbookbe.like.repository.LikeRepository;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
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
class LikeIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolderRepository folderRepository;

    User user;
    Folder folder;
    Like like;

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

        like = Like.builder()
                .folder(folder)
                .user(user)
                .build();
        likeRepository.save(like);
    }

    @AfterAll
    void teardown() {
        likeRepository.deleteAll();
        folderRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("좋아요 등록 테스트")
    void INSERT_LIKE_TEST() throws Exception {
        CreateLikeRequestDto likeRequestDto =
                CreateLikeRequestDto.builder()
                        .userId(user.getId())
                        .folderId(folder.getId())
                        .build();
        this.mockMvc.perform(post("/api/likes")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(likeRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(
                        document("register-like",
                                requestFields(
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
    @DisplayName("좋아요 삭제 테스트")
    void DELETE_LIKE_BY_ID_TEST() throws Exception {
        this.mockMvc.perform(put("/api/likes/{id}", like.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(
                        document("remove-by-id-like",
                                requestFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER)
                                                .description("likeId")
                                )
                        )
                );
    }
}
