package com.prgrms.team03linkbookbe.unit.folder.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.team03linkbookbe.annotation.WithJwtAuth;
import com.prgrms.team03linkbookbe.bookmark.repository.BookmarkRepository;
import com.prgrms.team03linkbookbe.config.WebSecurityConfigure;
import com.prgrms.team03linkbookbe.folder.controller.FolderController;
import com.prgrms.team03linkbookbe.folder.dto.CreateFolderRequest;
import com.prgrms.team03linkbookbe.folder.dto.FolderDetailResponse;
import com.prgrms.team03linkbookbe.folder.dto.FolderIdResponse;
import com.prgrms.team03linkbookbe.folder.dto.FolderListByUserResponse;
import com.prgrms.team03linkbookbe.folder.dto.FolderListResponse;
import com.prgrms.team03linkbookbe.folder.dto.FolderResponse;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import com.prgrms.team03linkbookbe.folder.service.FolderService;
import com.prgrms.team03linkbookbe.folderTag.service.FolderTagService;
import com.prgrms.team03linkbookbe.jwt.JwtAuthentication;
import com.prgrms.team03linkbookbe.user.dto.UserSimpleResponseDto;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = FolderController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigure.class)
    }
)
public class FolderControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FolderService service;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private FolderRepository folderRepository;

    @MockBean
    private FolderTagService folderTagService;

    @MockBean
    private BookmarkRepository bookmarkRepository;

    String accessToken = "access-token";

    UserSimpleResponseDto user = UserSimpleResponseDto.builder()
        .id(1L)
        .build();

    Page<FolderResponse> folders = new PageImpl<>(
        List.of(FolderResponse.builder()
            .id(1L)
            .build())
    );

    FolderDetailResponse folderDetailResponse = FolderDetailResponse.builder()
        .id(1L)
        .build();

    @Test
    @WithJwtAuth(email = "test@gmail.com")
    @DisplayName("폴더 전체를 조회할 수 있다.")
    void GET_ALL_FOLDER_TEST() throws Exception {
        when(service.getAll(any(Pageable.class), any(JwtAuthentication.class))).thenReturn(
            FolderListResponse.builder()
                .folders(folders)
                .build()
        );

        mockMvc.perform(get("/api/folders")
                .characterEncoding(StandardCharsets.UTF_8)
                .header("Access-Token", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", String.valueOf(0))
                .param("size", String.valueOf(12)))
            .andExpect(status().isOk());
    }

    @Test
    @WithJwtAuth(email = "test@gmail.com")
    @DisplayName("폴더를 제목으로 검색할 수 있다.")
    void GET_ALL_FOLDER_BY_TITLE_TEST() throws Exception {
        when(service.getAllByTitle(any(Pageable.class), eq("my"),
            any(JwtAuthentication.class))).thenReturn(
            FolderListResponse.builder()
                .folders(folders)
                .build()
        );

        mockMvc.perform(get("/api/folders")
                .characterEncoding(StandardCharsets.UTF_8)
                .header("Access-Token", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", String.valueOf(0))
                .param("size", String.valueOf(12))
                .param("title", "my"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("특정사용자의 폴더리스트를 조회할 수 있다.")
    void GET_ALL_FOLDER_BY_USER_TEST() throws Exception {
        when(service.getAllByUser(eq(1L), eq(null), eq("false"), any(Pageable.class)))
            .thenReturn(
                FolderListByUserResponse.builder()
                    .user(user)
                    .folders(folders)
                    .build()
            );

        mockMvc.perform(get("/api/folders/users/{userId}", 1L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", String.valueOf(0))
                .param("size", String.valueOf(12)))
            .andExpect(status().isOk());
    }

    @Test
    @WithJwtAuth(email = "test@gmail.com")
    @DisplayName("자신의 PRIVATE 폴더리스트 조회할 수 있다.")
    void GET_ALL_FOLDER_BY_USER_TEST_PRIVATE() throws Exception {
        when(service.getAllByUser(eq(1L), eq("test@gmail.com"), eq("true"), any(Pageable.class)))
            .thenReturn(
                FolderListByUserResponse.builder()
                    .user(user)
                    .folders(folders)
                    .build()
            );

        mockMvc.perform(get("/api/folders/users/{userId}", 1L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Access-Token", accessToken)
                .param("page", String.valueOf(0))
                .param("size", String.valueOf(12))
                .param("isPrivate", ("true")))
            .andExpect(status().isOk());
    }

    @Test
    @WithJwtAuth(email = "test@gmail.com")
    @DisplayName("특정폴더의 상세페이지를 조회할 수 있다.")
    void GET_DETAIL_OF_FOLDER_TEST() throws Exception {
        when(service.detail(eq(1L), any(JwtAuthentication.class)))
            .thenReturn(folderDetailResponse);

        mockMvc.perform(get("/api/folders/{folderId}", 1L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Access-Token", accessToken)
                .param("page", String.valueOf(0))
                .param("size", String.valueOf(12)))
            .andExpect(status().isOk());
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
                .tags(Lists.emptyList())
                .bookmarks(Lists.emptyList())
                .build();

        when(userRepository.findByEmail("test@gmail.com"))
            .thenReturn(Optional.of(User.builder().id(1L).build()));

        when(folderRepository.save(any(Folder.class)))
            .thenReturn(Folder.builder().id(1L).build());

        doNothing().when(folderTagService)
            .addFolderTag(any(CreateFolderRequest.class), any(Folder.class));

        mockMvc.perform(post("/api/folders")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Access-Token", accessToken)
                .content(objectMapper.writeValueAsString(createFolderRequest))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
            .andExpect(status().isOk());
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
                .tags(Lists.emptyList())
                .bookmarks(Lists.emptyList())
                .build();

        when(service.update(eq("test@gmail.com"), eq(1L), any(CreateFolderRequest.class)))
            .thenReturn(FolderIdResponse.builder().id(1L).build());

        mockMvc.perform(put("/api/folders/{id}", 1L)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Access-Token", accessToken)
                .content(objectMapper.writeValueAsString(createFolderRequest))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @WithJwtAuth(email = "test@gmail.com")
    @DisplayName("폴더를 삭제할 수 있다.")
    void DELETE_FOLDER_TEST() throws Exception {
        doNothing().when(service).delete("test@gmail.com", 1L);

        mockMvc.perform(delete("/api/folders/{id}", 1L)
                .characterEncoding(StandardCharsets.UTF_8)
                .header("Access-Token", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
            .andExpect(status().isOk());
    }

    @Test
    @WithJwtAuth(email = "test@gmail.com")
    @DisplayName("폴더를 루트태그로 검색할 수 있다.")
    void GET_ALL_FOLDER_BY_ROOT_TAG() throws Exception {
        String rootTag = "게임";

        when(service.getByRootTag(any(String.class), any(Pageable.class),
            any(JwtAuthentication.class)))
            .thenReturn(FolderListResponse.builder().folders(folders).build());

        mockMvc.perform(get("/api/folders/root-tag/{rootTag}", rootTag)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Access-Token", accessToken)
                .param("page", String.valueOf(0))
                .param("size", String.valueOf(12)))
            .andExpect(status().isOk());
    }

    @Test
    @WithJwtAuth(email = "test@gmail.com")
    @DisplayName("폴더를 서브태그로 검색할 수 있다.")
    void GET_ALL_FOLDER_BY_SUB_TAG() throws Exception {
        String tag = "게임1";

        when(service.getByTag(any(String.class), any(Pageable.class),
            any(JwtAuthentication.class)))
            .thenReturn(FolderListResponse.builder().folders(folders).build());

        mockMvc.perform(get("/api/folders/tag/{tag}", tag)
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Access-Token", accessToken)
                .param("page", String.valueOf(0))
                .param("size", String.valueOf(12))
                .param("sort", "likes"))
            .andExpect(status().isOk());
    }
}

