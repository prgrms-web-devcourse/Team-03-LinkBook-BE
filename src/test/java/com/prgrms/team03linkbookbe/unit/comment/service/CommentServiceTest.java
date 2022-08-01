package com.prgrms.team03linkbookbe.unit.comment.service;

import com.prgrms.team03linkbookbe.comment.dto.CreateCommentRequestDto;
import com.prgrms.team03linkbookbe.comment.dto.UpdateCommentRequestDto;
import com.prgrms.team03linkbookbe.comment.dto.UpdateCommentResponseDto;
import com.prgrms.team03linkbookbe.comment.entity.Comment;
import com.prgrms.team03linkbookbe.comment.repository.CommentRepository;
import com.prgrms.team03linkbookbe.comment.service.CommentService;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
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
class CommentServiceTest {
    @InjectMocks
    CommentService commentService;

    @Mock
    CommentRepository commentRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    FolderRepository folderRepository;

    CreateCommentRequestDto requestDto1;

    Folder folder;

    User user;

    @BeforeEach
    void setup() {
        requestDto1 = CreateCommentRequestDto.builder()
                .content("LGTM")
                .folderId(1L)
                .userId(1L)
                .build();

        folder = Folder.builder().build();

        user = User.builder().build();
    }

    @Test
    @DisplayName("댓글 작성 테스트")
    void INSERT_COMMENT_TEST() {
        // given
        when(folderRepository.findById(1L)).thenReturn(Optional.of(folder));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Comment comment = CreateCommentRequestDto.toEntity(folder, user, requestDto1);
        comment.toBuilder().id(1L).build();
        when(commentRepository.save(comment)).thenReturn(comment.toBuilder().id(1L).build());

        // when, then
        assertThat(commentService.create(requestDto1, 1L).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    void UPDATE_COMMENT_TEST() {
        // given
        when(folderRepository.findById(1L)).thenReturn(Optional.of(folder));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Comment comment = CreateCommentRequestDto.toEntity(folder, user, requestDto1);
        comment.toBuilder().id(1L).build();
        when(commentRepository.save(comment)).thenReturn(comment.toBuilder().id(1L).build());

        Long id = commentService.create(requestDto1, 1L).getId();
        UpdateCommentRequestDto updateCommentRequestDto = UpdateCommentRequestDto.builder()
                .id(id)
                .content("updated")
                .folderId(requestDto1.getFolderId())
                .userId(requestDto1.getUserId())
                .build();

        // when
        when(commentRepository.findByIdAndUser(id, user)).thenReturn(Optional.of(comment));
        UpdateCommentResponseDto updateCommentResponseDto =
                commentService.update(updateCommentRequestDto, 1L);

        // then
        assertThat(updateCommentResponseDto.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void DELETE_COMMENT_TEST() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(commentRepository.findByIdAndUser(1L, user))
                .thenReturn(Optional.of(Comment.builder().id(1L).build()));

        // when
        Long deleteComment = commentService.delete(1L, 1L);

        // then
        assertThat(deleteComment).isEqualTo(1L);
    }
}
