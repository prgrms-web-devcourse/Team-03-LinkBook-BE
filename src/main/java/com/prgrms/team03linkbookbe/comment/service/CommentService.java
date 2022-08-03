package com.prgrms.team03linkbookbe.comment.service;

import com.prgrms.team03linkbookbe.comment.dto.CreateCommentRequestDto;
import com.prgrms.team03linkbookbe.comment.dto.CreateCommentResponseDto;
import com.prgrms.team03linkbookbe.comment.dto.UpdateCommentRequestDto;
import com.prgrms.team03linkbookbe.comment.dto.UpdateCommentResponseDto;
import com.prgrms.team03linkbookbe.comment.entity.Comment;
import com.prgrms.team03linkbookbe.comment.repository.CommentRepository;
import com.prgrms.team03linkbookbe.common.exception.NoDataException;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    @Transactional
    public CreateCommentResponseDto create(CreateCommentRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(NoDataException::new);

        Folder folder = folderRepository.findById(requestDto.getFolderId())
                .orElseThrow(NoDataException::new);

        Comment comment = CreateCommentRequestDto.toEntity(folder, user, requestDto);

        return CreateCommentResponseDto.builder()
                .id(commentRepository.save(comment).getId())
                .build();
    }

    @Transactional
    public UpdateCommentResponseDto update(UpdateCommentRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(NoDataException::new);

        Folder folder = folderRepository.findById(requestDto.getFolderId())
                .orElseThrow(NoDataException::new);

        Comment comment = commentRepository.findByIdAndUser(requestDto.getId(), user)
                .orElseThrow(NoDataException::new);

        Comment updated = UpdateCommentRequestDto.toEntity(folder, user, comment);

        Comment save = commentRepository.save(updated);

        return UpdateCommentResponseDto.builder().id(save.getId()).build();
    }

    @Transactional
    public Long delete(Long id, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(NoDataException::new);

        Comment comment = commentRepository.findByIdAndUser(id, user)
                .orElseThrow(NoDataException::new);

        commentRepository.delete(comment);

        return comment.getId();
    }
}
