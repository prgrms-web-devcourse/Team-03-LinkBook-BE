package com.prgrms.team03linkbookbe.unit.bookmark.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.team03linkbookbe.bookmark.controller.BookmarkController;
import com.prgrms.team03linkbookbe.bookmark.dto.BookmarkRequest;
import com.prgrms.team03linkbookbe.bookmark.service.BookmarkService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class BookmarkControllerTest {

    @InjectMocks
    private BookmarkController bookmarkController;

    @Mock
    private BookmarkService bookmarkService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;


    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookmarkController).build();
    }


    @Test
    @DisplayName("북마크를 생성할 수 있다.")
    void CREATE_BOOKMARK_TEST() throws Exception{
        BookmarkRequest request = BookmarkRequest.builder()
            .title("test")
            .url("test.com")
            .folderId(1L)
            .build();

        doNothing().when(bookmarkService).create(any(BookmarkRequest.class));

        mockMvc.perform(post("/api/bookmarks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());


    }

    @Test
    @DisplayName("북마크를 수정할 수 있다.")
    void UPDATE_BOOKMARK_TEST(){
        BookmarkRequest.builder()
            .title("test")
            .url("test.com")
            .folderId(1L)
            .build();

        doNothing().when(bookmarkService).create(any(BookmarkRequest.class));


    }

    @Test
    @DisplayName("북마크를 삭제할 수 있다.")
    void DELETE_BOOKMARK_TEST(){

    }







}
