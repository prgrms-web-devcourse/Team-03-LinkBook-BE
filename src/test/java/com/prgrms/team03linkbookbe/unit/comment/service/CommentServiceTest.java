package com.prgrms.team03linkbookbe.unit.comment.service;

import com.prgrms.team03linkbookbe.comment.dto.CreateCommentRequestDto;
import com.prgrms.team03linkbookbe.comment.repository.CommentRepository;
import com.prgrms.team03linkbookbe.comment.service.CommentService;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @InjectMocks
    CommentService commentService;

    @Mock
    CommentRepository commentRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    FolderRepository folderRepository;

    CreateCommentRequestDto requestDto1;

    @BeforeAll
    void setup() {
        requestDto1 = CreateCommentRequestDto.builder()
                .content("LGTM")
                .folderId(1L)
                .userId(1L)
                .build();
    }

    @Test
    @DisplayName("댓글 작성 테스트")
    void INSERT_COMMENT_TEST() {
        // given
        when(folderRepository.findById(1L)).thenReturn(Optional.of(Folder.builder().build()));
        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().build()));

        // when, then
        assertThat(commentService.saveComment(requestDto1).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("대댓글 작성 테스트")
    void INSERT_REPLY_COMMENT_TEST() {
        // given
        when(folderRepository.findById(1L)).thenReturn(Optional.of(Folder.builder().build()));
        when(userRepository.findById(2L)).thenReturn(Optional.of(User.builder().build()));
        Long id = commentService.saveComment(requestDto1).getId();
        CreateCommentRequestDto requestDto2 = CreateCommentRequestDto.builder()
                .content("LGTM")
                .folderId(1L)
                .parentId(id)
                .userId(2L)
                .build();

        // when, then
        assertThat(commentService.saveComment(requestDto2).getId()).isEqualTo(2L);
    }
}
