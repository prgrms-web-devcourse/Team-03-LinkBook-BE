package com.prgrms.team03linkbookbe.integration.bookmark;


import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.team03linkbookbe.annotation.WithJwtAuth;
import com.prgrms.team03linkbookbe.bookmark.dto.BookmarkRequest;
import com.prgrms.team03linkbookbe.bookmark.entity.Bookmark;
import com.prgrms.team03linkbookbe.bookmark.repository.BookmarkRepository;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import net.bytebuddy.utility.dispatcher.JavaDispatcher.Container;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
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
@TestInstance(Lifecycle.PER_CLASS)
public class BookmarkIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private UserRepository userRepository;

    private Bookmark bookmark;

    private Bookmark bookmarkForDelete;

    private Folder folder;

    private String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaXNzIjoibGluay1ib29rIiwiZXhwIjoxNjU5MDc4MzE2LCJpYXQiOjE2NTkwNzQ3MTYsImVtYWlsIjoidXNlcjFAZ21haWwuY29tIn0.ksk7dW4Z4grAkWKeryEfJbwA4HvqApCk3I7afAO4Ir0CR2NeL3Oe0YbgZCtwRXM3EtB0RPqtJCMfAP_L6pDVKQ";


    @BeforeAll
    void init() {
        User user = User.builder()
            .image("img")
            .email("test@gmail.com")
            .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
            .role("USER")
            .name("tester")
            .introduce("hello")
            .build();

        userRepository.save(user);

        folder = Folder.builder()
            .title("my-folder")
            .image("url")
            .content("halo")
            .likes(0)
            .isPinned(false)
            .isPrivate(false)
            .user(user)
            .build();

        folderRepository.save(folder);

        bookmark = Bookmark.builder()
            .url("url")
            .title("test")
            .folder(folder)
            .build();

        bookmarkRepository.save(bookmark);

        bookmarkForDelete = Bookmark.builder()
            .url("url")
            .title("test")
            .folder(folder)
            .build();

        bookmarkRepository.save(bookmarkForDelete);
    }

    @AfterAll
    void tear_down() {
        bookmarkRepository.deleteAll();
        folderRepository.deleteAll();
    }


    @Test
    @WithJwtAuth(email = "test@gmail.com")
    @DisplayName("북마크를 생성할 수 있다.")
    void CREATE_BOOKMARK_TEST() throws Exception {
        BookmarkRequest dto = BookmarkRequest.builder()
            .url("url1")
            .folderId(folder.getId())
            .title("title1")
            .build();

        mockMvc.perform(post("/api/bookmarks")
            .characterEncoding(StandardCharsets.UTF_8)
            .header("Access-Token", accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("create-bookmark",
                requestHeaders(
                    headerWithName("Access-Token")
                        .description("access token, 필수")
                ),
                requestFields(
                    fieldWithPath("id").type(JsonFieldType.NULL)
                        .description("id, 입력하지마시오"),
                    fieldWithPath("url").type(JsonFieldType.STRING)
                        .description("url"),
                    fieldWithPath("title").type(JsonFieldType.STRING)
                        .description("title"),
                    fieldWithPath("folderId").type(JsonFieldType.NUMBER)
                        .description("folderId")
                )

            ));


    }


    @Test
    @WithJwtAuth(email = "test@gmail.com")
    @DisplayName("북마크를 수정할 수 있다.")
    void UPDATE_BOOKMARK_TEST() throws Exception {
        BookmarkRequest dto = BookmarkRequest.builder()
            .url("수정url")
            .folderId(bookmark.getFolder().getId())
            .title("수정title")
            .build();

        mockMvc.perform(put("/api/bookmarks/{id}", bookmark.getId())
            .characterEncoding(StandardCharsets.UTF_8)
            .header("Access-Token", accessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("update-bookmark",
                requestHeaders(
                    headerWithName("Access-Token")
                        .description("access token, 필수")
                ),
                requestFields(
                    fieldWithPath("id").type(JsonFieldType.NULL)
                        .description("id, 입력하지마시오"),
                    fieldWithPath("url").type(JsonFieldType.STRING)
                        .description("url"),
                    fieldWithPath("title").type(JsonFieldType.STRING)
                        .description("title"),
                    fieldWithPath("folderId").type(JsonFieldType.NUMBER)
                        .description("folderId")
                )

            ));


    }

    @Test
    @WithJwtAuth(email = "test@gmail.com")
    @DisplayName("북마크를 삭제할 수 있다.")
    void DELETE_BOOKMARK_TEST() throws Exception {
        mockMvc.perform(delete("/api/bookmarks/{id}", bookmarkForDelete.getId())
            .characterEncoding(StandardCharsets.UTF_8)
            .header("Access-Token", accessToken)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("create-bookmark",
                requestHeaders(
                    headerWithName("Access-Token")
                        .description("access token, 필수")
                ))

            );


    }

}
