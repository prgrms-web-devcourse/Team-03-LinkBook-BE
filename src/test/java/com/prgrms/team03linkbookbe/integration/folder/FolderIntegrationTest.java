package com.prgrms.team03linkbookbe.integration.folder;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.team03linkbookbe.annotation.WithJwtAuth;
import com.prgrms.team03linkbookbe.bookmark.dto.BookmarkRequest;
import com.prgrms.team03linkbookbe.bookmark.entity.Bookmark;
import com.prgrms.team03linkbookbe.bookmark.repository.BookmarkRepository;
import com.prgrms.team03linkbookbe.folder.dto.CreateFolderRequest;
import com.prgrms.team03linkbookbe.folder.dto.RootTagRequest;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import com.prgrms.team03linkbookbe.folderTag.entity.FolderTag;
import com.prgrms.team03linkbookbe.folderTag.repository.FolderTagRepository;
import com.prgrms.team03linkbookbe.rootTag.entity.RootTag;
import com.prgrms.team03linkbookbe.rootTag.entity.RootTagCategory;
import com.prgrms.team03linkbookbe.rootTag.repository.RootTagRepository;
import com.prgrms.team03linkbookbe.tag.entity.Tag;
import com.prgrms.team03linkbookbe.tag.entity.TagCategory;
import com.prgrms.team03linkbookbe.tag.repository.TagRepository;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
public class FolderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private FolderTagRepository folderTagRepository;

    @Autowired
    private RootTagRepository rootTagRepository;

    private User user;

    private Folder folderForModify;

    private Folder folderForDelete;

    private RootTag rootTag;

    private Tag tag;

    private Bookmark bookmark;

    private String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaXNzIjoibGluay1ib29rIiwiZXhwIjoxNjU5MDc4MzE2LCJpYXQiOjE2NTkwNzQ3MTYsImVtYWlsIjoidXNlcjFAZ21haWwuY29tIn0.ksk7dW4Z4grAkWKeryEfJbwA4HvqApCk3I7afAO4Ir0CR2NeL3Oe0YbgZCtwRXM3EtB0RPqtJCMfAP_L6pDVKQ";


    @BeforeEach
    void init() {
        user = User.builder()
            .image("img")
            .email("test@gmail.com")
            .password("$2a$12$8zS0i9eXSnKN.jXY1cqOhOxrAQvhsh5WMtJmOsfnQIaHMZudKmmKa")
            .role("USER")
            .name("tester")
            .introduce("hello")
            .build();

        userRepository.save(user);

        rootTag = RootTag.builder()
            .name(RootTagCategory.GAME)
            .build();

        rootTagRepository.save(rootTag);

        tag = Tag.builder()
            .rootTag(rootTag)
            .name(TagCategory.GAME1)
            .build();

        tagRepository.save(tag);

        folderForModify = Folder.builder()
            .title("my-folder")
            .image("url")
            .content("halo")
            .likes(0)
            .isPinned(false)
            .isPrivate(false)
            .user(user)
            .build();

        folderRepository.save(folderForModify);

        folderForDelete = Folder.builder()
            .title("my-folder2")
            .image("url")
            .content("halo")
            .likes(0)
            .isPinned(false)
            .isPrivate(true)
            .user(user)
            .build();

        folderRepository.save(folderForDelete);

        FolderTag folderTag = FolderTag.builder()
            .folder(folderForModify)
            .tag(tag)
            .build();

        folderTagRepository.save(folderTag);

        folderForDelete.updateFolderTags(List.of(folderTag));

        bookmark = Bookmark.builder()
            .url("url")
            .title("test")
            .folder(folderForModify)
            .build();

        bookmarkRepository.save(bookmark);
    }


    @AfterEach
    void tear_down() {
        folderTagRepository.deleteAll();
        tagRepository.deleteAll();
        rootTagRepository.deleteAll();
        folderRepository.deleteAll();
        userRepository.deleteAll();
        bookmarkRepository.deleteAll();
    }


    @Test
    @WithJwtAuth(email = "test@gmail.com")
    @DisplayName("폴더 전체를 조회할 수 있다.")
    void GET_ALL_FOLDER_TEST() throws Exception {
        mockMvc.perform(get("/api/folders")
                .characterEncoding(StandardCharsets.UTF_8)
                .header("Access-Token", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", String.valueOf(0))
                .param("size", String.valueOf(12)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andDo(
                document("find-all-folder-list",
                    requestHeaders(
                        headerWithName("Access-Token")
                            .description("access token, 없으면 좋아요 여부 없음")
                    ),
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
                        fieldWithPath("folders.content[].user").type(JsonFieldType.OBJECT)
                            .description("folders.content[].user"),
                        fieldWithPath("folders.content[].user.id").type(JsonFieldType.NUMBER)
                            .description("folders.content[].user.id"),
                        fieldWithPath("folders.content[].user.email").type(JsonFieldType.STRING)
                            .description("folders.content[].user.email"),
                        fieldWithPath("folders.content[].user.name").type(JsonFieldType.STRING)
                            .description("folders.content[].user.name"),
                        fieldWithPath("folders.content[].user.image").type(JsonFieldType.STRING)
                            .description("folders.content[].user.image"),
                        fieldWithPath("folders.content[].user.introduce").type(JsonFieldType.STRING)
                            .description("folders.content[].user.introduce,(STRING, can be NULL)"),
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
    @WithJwtAuth(email = "test@gmail.com")
    @DisplayName("폴더를 제목으로 검색할 수 있다.")
    void GET_ALL_FOLDER_BY_TITLE_TEST() throws Exception {
        mockMvc.perform(get("/api/folders")
                .characterEncoding(StandardCharsets.UTF_8)
                .header("Access-Token", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", String.valueOf(0))
                .param("size", String.valueOf(12))
                .param("title", "my"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andDo(
                document("find-all-folder-list-by-title",
                    requestHeaders(
                        headerWithName("Access-Token")
                            .description("access token, 없으면 좋아요 여부 없음")
                    ),
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
                        fieldWithPath("folders.content[].user").type(JsonFieldType.OBJECT)
                            .description("folders.content[].user"),
                        fieldWithPath("folders.content[].user.id").type(JsonFieldType.NUMBER)
                            .description("folders.content[].user.id"),
                        fieldWithPath("folders.content[].user.email").type(JsonFieldType.STRING)
                            .description("folders.content[].user.email"),
                        fieldWithPath("folders.content[].user.name").type(JsonFieldType.STRING)
                            .description("folders.content[].user.name"),
                        fieldWithPath("folders.content[].user.image").type(JsonFieldType.STRING)
                            .description("folders.content[].user.image"),
                        fieldWithPath("folders.content[].user.introduce").type(JsonFieldType.STRING)
                            .description("folders.content[].user.introduce, (STRING, can be NULL)"),
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
    @DisplayName("특정사용자의 폴더리스트를 조회할 수 있다.")
    void GET_ALL_FOLDER_BY_USER_TEST() throws Exception {
        mockMvc.perform(get("/api/folders/users/{userId}", user.getId())
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", String.valueOf(0))
                .param("size", String.valueOf(12)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andDo(
                document("find-all-folder-list-by-user",
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
                        fieldWithPath("user.introduce").type(JsonFieldType.STRING)
                            .description("user.introduce,(STRING, can be NULL)"),
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
                        fieldWithPath("folders.content[].user").type(JsonFieldType.OBJECT)
                            .description("folders.content[].user"),
                        fieldWithPath("folders.content[].user.id").type(JsonFieldType.NUMBER)
                            .description("folders.content[].user.id"),
                        fieldWithPath("folders.content[].user.email").type(JsonFieldType.STRING)
                            .description("folders.content[].user.email"),
                        fieldWithPath("folders.content[].user.name").type(JsonFieldType.STRING)
                            .description("folders.content[].user.name"),
                        fieldWithPath("folders.content[].user.image").type(JsonFieldType.STRING)
                            .description("folders.content[].user.image"),
                        fieldWithPath("folders.content[].user.introduce").type(JsonFieldType.STRING)
                            .description("folders.content[].user.introduce,(STRING, can be NULL)"),
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
    @WithJwtAuth(email = "test@gmail.com")
    @DisplayName("자신의 PRIVATE 폴더리스트 조회할 수 있다.")
    void GET_ALL_FOLDER_BY_USER_TEST_PRIVATE() throws Exception {
        mockMvc.perform(get("/api/folders/users/{userId}", user.getId())
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Access-Token", accessToken)
                .param("page", String.valueOf(0))
                .param("size", String.valueOf(12))
                .param("isPrivate", ("true")))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andDo(
                document("find-all-private-folder-list-by-user",
                    requestHeaders(
                        headerWithName("Access-Token")
                            .description("access token, 필수")
                    ),
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
                        fieldWithPath("user.introduce").type(JsonFieldType.STRING)
                            .description("user.introduce,(STRING, can be NULL)"),
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
                        fieldWithPath("folders.content[].user").type(JsonFieldType.OBJECT)
                            .description("folders.content[].user"),
                        fieldWithPath("folders.content[].user.id").type(JsonFieldType.NUMBER)
                            .description("folders.content[].user.id"),
                        fieldWithPath("folders.content[].user.email").type(JsonFieldType.STRING)
                            .description("folders.content[].user.email"),
                        fieldWithPath("folders.content[].user.name").type(JsonFieldType.STRING)
                            .description("folders.content[].user.name"),
                        fieldWithPath("folders.content[].user.image").type(JsonFieldType.STRING)
                            .description("folders.content[].user.image"),
                        fieldWithPath("folders.content[].user.introduce").type(JsonFieldType.STRING)
                            .description("folders.content[].user.introduce,(STRING, can be NULL)"),
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
    @WithJwtAuth(email = "test@gmail.com")
    @DisplayName("특정폴더의 상세페이지를 조회할 수 있다.")
    void GET_DETAIL_OF_FOLDER_TEST() throws Exception {
        mockMvc.perform(get("/api/folders/{folderId}", folderForModify.getId())
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Access-Token", accessToken)
                .param("page", String.valueOf(0))
                .param("size", String.valueOf(12)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andDo(
                document("find-folder-detail",
                    requestHeaders(
                        headerWithName("Access-Token")
                            .description("access token, 없으면 좋아요 여부 없음")
                    ),
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER)
                            .description("fid"),
                        fieldWithPath("title").type(JsonFieldType.STRING)
                            .description("title"),
                        fieldWithPath("image").type(JsonFieldType.STRING)
                            .description("image"),
                        fieldWithPath("content").type(JsonFieldType.STRING)
                            .description("content"),
                        fieldWithPath("isPinned").type(JsonFieldType.BOOLEAN)
                            .description("isPinned"),
                        fieldWithPath("isPrivate").type(JsonFieldType.BOOLEAN)
                            .description("isPrivate"),
                        fieldWithPath("originFolder").type(JsonFieldType.NULL)
                            .description("originFolder"),
                        fieldWithPath("bookmarks[]").type(JsonFieldType.ARRAY)
                            .description("bookmarks[]"),
                        fieldWithPath("bookmarks[].id").type(JsonFieldType.NUMBER)
                            .description("bookmarks[].id"),
                        fieldWithPath("bookmarks[].url").type(JsonFieldType.STRING)
                            .description("bookmarks[].url"),
                        fieldWithPath("bookmarks[].title").type(JsonFieldType.STRING)
                            .description("bookmarks[].title"),
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
                        fieldWithPath("user.introduce").type(JsonFieldType.STRING)
                            .description("user.introduce, (STRING, can be NULL)"),
                        fieldWithPath("tags[]").type(JsonFieldType.ARRAY)
                            .description("tags[]"),
                        fieldWithPath("likes").type(JsonFieldType.NUMBER)
                            .description("likes"),
                        fieldWithPath("isLiked").type(JsonFieldType.BOOLEAN)
                            .description("isLiked"),
                        fieldWithPath("createdAt").type(JsonFieldType.STRING)
                            .description("createdAt")

                    )

                )
            );


    }


    @Test
    @WithJwtAuth(email = "test@gmail.com")
    @DisplayName("폴더를 생성할 수 있다.")
    void CREATE_FOLDER_TEST() throws Exception {
        CreateFolderRequest createFolderRequest =
            CreateFolderRequest.builder()
                .title("my-folder2")
                .image("url")
                .content("halo")
                .isPinned(false)
                .isPrivate(true)
                .tags(folderForModify.getFolderTags().stream().map(o -> o.getTag().getName())
                    .collect(Collectors.toList()))
                .bookmarks(folderForModify.getBookmarks().stream()
                    .map(o -> BookmarkRequest
                        .builder()
                        .url(o.getUrl())
                        .title(o.getTitle())
                        .build())
                    .collect(Collectors.toList()))
                .build();
        this.mockMvc.perform(post("/api/folders")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Access-Token", accessToken)
                .content(objectMapper.writeValueAsString(createFolderRequest)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(
                document("create-folder",
                    requestHeaders(
                        headerWithName("Access-Token")
                            .description("access token, 필수")
                    ),
                    requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING)
                            .description("title"),
                        fieldWithPath("image").type(JsonFieldType.STRING)
                            .description("image"),
                        fieldWithPath("content").type(JsonFieldType.STRING)
                            .description("content"),
                        fieldWithPath("isPinned").type(JsonFieldType.BOOLEAN)
                            .description("isPinned"),
                        fieldWithPath("isPrivate").type(JsonFieldType.BOOLEAN)
                            .description("isPrivate"),
                        fieldWithPath("tags").type(JsonFieldType.ARRAY)
                            .description("tags"),
                        fieldWithPath("bookmarks").type(JsonFieldType.ARRAY)
                            .description("bookmarks"),
                        fieldWithPath("originId").type(JsonFieldType.NULL)
                            .description("originId, can be NULL")
                    ),
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER)
                            .description("id")
                    )
                )
            );
    }

    @Test
    @WithJwtAuth(email = "test@gmail.com")
    @DisplayName("폴더를 수정할 수 있다.")
    void UPDATE_FOLDER_TEST() throws Exception {
        CreateFolderRequest createFolderRequest =
            CreateFolderRequest.builder()
                .title("수정title")
                .image("수정img")
                .content("수정content")
                .isPinned(false)
                .isPrivate(false)
                .tags(folderForModify.getFolderTags().stream().map(o -> o.getTag().getName())
                    .collect(Collectors.toList()))
                .bookmarks(folderForModify.getBookmarks().stream()
                    .map(o -> BookmarkRequest
                        .builder()
                        .id(o.getId())
                        .folderId(o.getFolder().getId())
                        .url(o.getUrl())
                        .title(o.getTitle())
                        .build())
                    .collect(Collectors.toList()))
                .build();
        this.mockMvc.perform(put("/api/folders/{id}", folderForModify.getId())
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Access-Token", accessToken)
                .content(objectMapper.writeValueAsString(createFolderRequest)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(
                document("update-folder",
                    requestHeaders(
                        headerWithName("Access-Token")
                            .description("access token, 필수")
                    ),
                    requestFields(
                        fieldWithPath("title").type(JsonFieldType.STRING)
                            .description("title"),
                        fieldWithPath("image").type(JsonFieldType.STRING)
                            .description("image"),
                        fieldWithPath("content").type(JsonFieldType.STRING)
                            .description("content"),
                        fieldWithPath("isPinned").type(JsonFieldType.BOOLEAN)
                            .description("isPinned"),
                        fieldWithPath("isPrivate").type(JsonFieldType.BOOLEAN)
                            .description("isPrivate"),
                        fieldWithPath("tags").type(JsonFieldType.ARRAY)
                            .description("tags"),
                        fieldWithPath("bookmarks").type(JsonFieldType.ARRAY)
                            .description("bookmarks"),
                        fieldWithPath("originId").type(JsonFieldType.NULL)
                            .description("originId, can be NULL")
                    ),
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER)
                            .description("id")
                    )
                )
            );
    }


    @Test
    @WithJwtAuth(email = "test@gmail.com")
    @DisplayName("폴더를 삭제할 수 있다.")
    void DELETE_FOLDER_TEST() throws Exception {
        this.mockMvc.perform(delete("/api/folders/{id}", folderForDelete.getId())
                .characterEncoding(StandardCharsets.UTF_8)
                .header("Access-Token", accessToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(
                document("delete-folder",
                    requestHeaders(
                        headerWithName("Access-Token")
                            .description("access token, 필수")
                    ))
            );
    }

    @Test
    @WithJwtAuth(email = "test@gmail.com")
    @DisplayName("폴더를 루트태그로 검색할 수 있다.")
    void GET_ALL_FOLDER_BY_ROOT_TAG() throws Exception {
        String request = "{\"rootTag\" : \"게임\"}";

        mockMvc.perform(get("/api/folders/root-tag")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Access-Token", accessToken)
                .content(request)
                .param("page", String.valueOf(0))
                .param("size", String.valueOf(12)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andDo(
                document("find-all-folder-list-by-root-tag",
                    requestHeaders(
                        headerWithName("Access-Token")
                            .description("access token, 없으면 좋아요 여부 없음")
                    ),
                    requestFields(
                        fieldWithPath("rootTag").type(JsonFieldType.STRING)
                            .description("rootTag")
                    ),
                    responseFields(
                        fieldWithPath("folders").type(JsonFieldType.OBJECT)
                            .description("folders"),
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
                        fieldWithPath("folders.content[].user").type(JsonFieldType.OBJECT)
                            .description("folders.content[].user"),
                        fieldWithPath("folders.content[].user.id").type(JsonFieldType.NUMBER)
                            .description("folders.content[].user.id"),
                        fieldWithPath("folders.content[].user.email").type(JsonFieldType.STRING)
                            .description("folders.content[].user.email"),
                        fieldWithPath("folders.content[].user.name").type(JsonFieldType.STRING)
                            .description("folders.content[].user.name"),
                        fieldWithPath("folders.content[].user.image").type(JsonFieldType.STRING)
                            .description("folders.content[].user.image"),
                        fieldWithPath("folders.content[].user.introduce").type(JsonFieldType.STRING)
                            .description("folders.content[].user.introduce,(STRING, can be NULL)"),
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
    @WithJwtAuth(email = "test@gmail.com")
    @DisplayName("폴더를 서브태그로 검색할 수 있다.")
    void GET_ALL_FOLDER_BY_SUB_TAG() throws Exception {
        String request = "{\"tag\" : \"게임1\"}";

        mockMvc.perform(get("/api/folders/tag")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Access-Token", accessToken)
                .content(request)
                .param("page", String.valueOf(0))
                .param("size", String.valueOf(12))
                .param("sort", "likes"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andDo(
                document("find-all-folder-list-by-sub-tag",
                    requestHeaders(
                        headerWithName("Access-Token")
                            .description("access token, 없으면 좋아요 여부 없음")
                    ),
                    requestFields(
                        fieldWithPath("tag").type(JsonFieldType.STRING)
                            .description("tag")
                    ),
                    responseFields(
                        fieldWithPath("folders").type(JsonFieldType.OBJECT)
                            .description("folders"),
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
                        fieldWithPath("folders.content[].user").type(JsonFieldType.OBJECT)
                            .description("folders.content[].user"),
                        fieldWithPath("folders.content[].user.id").type(JsonFieldType.NUMBER)
                            .description("folders.content[].user.id"),
                        fieldWithPath("folders.content[].user.email").type(JsonFieldType.STRING)
                            .description("folders.content[].user.email"),
                        fieldWithPath("folders.content[].user.name").type(JsonFieldType.STRING)
                            .description("folders.content[].user.name"),
                        fieldWithPath("folders.content[].user.image").type(JsonFieldType.STRING)
                            .description("folders.content[].user.image"),
                        fieldWithPath("folders.content[].user.introduce").type(JsonFieldType.STRING)
                            .description("folders.content[].user.introduce,(STRING, can be NULL)"),
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
}
