package com.prgrms.team03linkbookbe.comment.service;

import com.prgrms.team03linkbookbe.comment.dto.*;
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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    @Transactional
    public CreateCommentResponseDto saveComment(CreateCommentRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(NoDataException::new);

        Folder folder = folderRepository.findById(requestDto.getFolderId())
                .orElseThrow(NoDataException::new);

        Comment comment = CreateCommentRequestDto.toEntity(folder, user, requestDto);

        Comment save = commentRepository.save(comment);

        return CreateCommentResponseDto.builder().id(save.getId()).build();
    }

    @Transactional
    public List<CommentResponseDto> findCommentsByFolder(Long folderId) {
        return commentRepository.findCommentFetchJoinByFolderId(folderId)
                .stream()
                .map(CommentResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public UpdateCommentResponseDto updateComment(UpdateCommentRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(NoDataException::new);

        Folder folder = folderRepository.findById(requestDto.getFolderId())
                .orElseThrow(NoDataException::new);

        Comment comment = commentRepository.findById(requestDto.getId())
                .orElseThrow(NoDataException::new);

        Comment updated = UpdateCommentRequestDto.toEntity(folder, user, comment);

        Comment save = commentRepository.save(updated);

        return UpdateCommentResponseDto.builder().id(save.getFolder().getId()).build();
    }

    @Transactional
    public Long deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(NoDataException::new);

        commentRepository.delete(comment);

        return comment.getId();
    }
}
