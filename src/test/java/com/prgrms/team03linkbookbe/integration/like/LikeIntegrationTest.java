package com.prgrms.team03linkbookbe.integration.like;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.team03linkbookbe.annotation.WithJwtAuth;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import com.prgrms.team03linkbookbe.like.dto.CreateLikeRequest;
import com.prgrms.team03linkbookbe.like.entity.Like;
import com.prgrms.team03linkbookbe.like.repository.LikeRepository;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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

        User temp = User.builder()
                .email("temp@test.com")
                .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
                .image("url")
                .role("USER")
                .name("temp")
                .build();
        userRepository.save(temp);

        folder = Folder.builder()
                .title("my-folder")
                .image("url")
                .content("halo")
                .likes(0)
                .isPinned(false)
                .isPrivate(false)
                .user(temp)
                .build();
        folderRepository.save(folder);

        Folder tempFolder = Folder.builder()
                .title("my-folder")
                .image("url")
                .content("halo")
                .likes(0)
                .isPinned(false)
                .isPrivate(false)
                .user(temp)
                .build();
        folderRepository.save(tempFolder);

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
    @WithJwtAuth(email = "test@test.com")
    void INSERT_LIKE_TEST() throws Exception {
        CreateLikeRequest likeRequestDto =
                CreateLikeRequest.builder()
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
                                ),
                                responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER)
                                                .description("likeId")
                                )
                        )
                );
    }

    @Test
    @Order(2)
    @DisplayName("좋아요한 폴더 목록 테스트")
    @WithJwtAuth(email = "test@test.com")
    void FIND_LIKED_FOLDER_LIST_TEST() throws Exception {
        this.mockMvc.perform(get("/api/likes/{id}", user.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(10)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(
                        document("find-liked-folder-list",
                                responseFields(
                                        fieldWithPath("folders.content[]").type(JsonFieldType.ARRAY)
                                                .description("folders.content[]"),
                                        fieldWithPath("folders.content[].id").type(JsonFieldType.NUMBER)
                                                .description("folders.content[].id"),
                                        fieldWithPath("folders.content[].title").type(JsonFieldType.STRING)
                                                .description("folders.content[].title"),
                                        fieldWithPath("folders.content[].image").type(JsonFieldType.STRING)
                                                .description("folders.content[].image"),
                                        fieldWithPath("folders.content[].content").type(JsonFieldType.NULL)
                                                .description("folders.content[].content"),
                                        fieldWithPath("folders.content[].isPinned").type(JsonFieldType.BOOLEAN)
                                                .description("folders.content[].isPinned"),
                                        fieldWithPath("folders.content[].isPrivate").type(JsonFieldType.BOOLEAN)
                                                .description("folders.content[].isPrivate"),
                                        fieldWithPath("folders.content[].user.id").type(JsonFieldType.NUMBER)
                                                .description("folders.content[].user.id"),
                                        fieldWithPath("folders.content[].user.email").type(JsonFieldType.STRING)
                                                .description("folders.content[].user.email"),
                                        fieldWithPath("folders.content[].user.name").type(JsonFieldType.STRING)
                                                .description("folders.content[].user.name"),
                                        fieldWithPath("folders.content[].user.image").type(JsonFieldType.STRING)
                                                .description("folders.content[].user.image"),
                                        fieldWithPath("folders.content[].user.introduce").type(JsonFieldType.NULL)
                                                .description("folders.content[].user.introduce"),
                                        fieldWithPath("folders.content[].tags[]").type(JsonFieldType.ARRAY)
                                                .description("folders.content[].tags[]"),
                                        fieldWithPath("folders.content[].likes").type(JsonFieldType.NUMBER)
                                                .description("folders.content[].likes"),
                                        fieldWithPath("folders.content[].isLiked").type(JsonFieldType.BOOLEAN)
                                                .description("folders.content[].isLiked"),
                                        fieldWithPath("folders.content[].createdAt").type(JsonFieldType.STRING)
                                                .description("folders.content[].createdAt"),
                                        fieldWithPath("folders.pageable").type(JsonFieldType.OBJECT)
                                                .description("folders.pageable"),
                                        fieldWithPath("folders.pageable.sort").type(JsonFieldType.OBJECT)
                                                .description("folders.pageable.sort"),
                                        fieldWithPath("folders.pageable.sort.empty").type(JsonFieldType.BOOLEAN)
                                                .description("folders.pageable.sort.empty"),
                                        fieldWithPath("folders.pageable.sort.sorted").type(JsonFieldType.BOOLEAN)
                                                .description("folders.pageable.sort.sorted"),
                                        fieldWithPath("folders.pageable.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                                .description("folders.pageable.sort.unsorted"),
                                        fieldWithPath("folders.pageable.offset").type(JsonFieldType.NUMBER)
                                                .description("folders.pageable.offset"),
                                        fieldWithPath("folders.pageable.pageNumber").type(JsonFieldType.NUMBER)
                                                .description("folders.pageable.pageNumber"),
                                        fieldWithPath("folders.pageable.pageSize").type(JsonFieldType.NUMBER)
                                                .description("folders.pageable.pageSize"),
                                        fieldWithPath("folders.pageable.paged").type(JsonFieldType.BOOLEAN)
                                                .description("folders.pageable.paged"),
                                        fieldWithPath("folders.pageable.unpaged").type(JsonFieldType.BOOLEAN)
                                                .description("folders.pageable.unpaged"),
                                        fieldWithPath("folders.last").type(JsonFieldType.BOOLEAN)
                                                .description("folders.last"),
                                        fieldWithPath("folders.totalPages").type(JsonFieldType.NUMBER)
                                                .description("folders.totalPages"),
                                        fieldWithPath("folders.totalElements").type(JsonFieldType.NUMBER)
                                                .description("folders.totalElements"),
                                        fieldWithPath("folders.size").type(JsonFieldType.NUMBER)
                                                .description("folders.size"),
                                        fieldWithPath("folders.number").type(JsonFieldType.NUMBER)
                                                .description("folders.number"),
                                        fieldWithPath("folders.sort").type(JsonFieldType.OBJECT)
                                                .description("folders.sort"),
                                        fieldWithPath("folders.sort.empty").type(JsonFieldType.BOOLEAN)
                                                .description("folders.sort.empty"),
                                        fieldWithPath("folders.sort.sorted").type(JsonFieldType.BOOLEAN)
                                                .description("folders.sort.sorted"),
                                        fieldWithPath("folders.sort.unsorted").type(JsonFieldType.BOOLEAN)
                                                .description("folders.sort.unsorted"),
                                        fieldWithPath("folders.first").type(JsonFieldType.BOOLEAN)
                                                .description("folders.first"),
                                        fieldWithPath("folders.numberOfElements").type(JsonFieldType.NUMBER)
                                                .description("folders.numberOfElements"),
                                        fieldWithPath("folders.empty").type(JsonFieldType.BOOLEAN)
                                                .description("folders.empty")
                                )
                        )
                );
    }

    @Test
    @Order(3)
    @DisplayName("좋아요 삭제 테스트")
    @WithJwtAuth(email = "test@test.com")
    void DELETE_LIKE_BY_ID_TEST() throws Exception {
        this.mockMvc.perform(delete("/api/likes/{folderId}", folder.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        document("remove-by-id-like")
                );
    }
}
