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
    public CreateCommentResponse create(CreateCommentRequest requestDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NoDataException::new);

        Folder folder = folderRepository.findById(requestDto.getFolderId())
                .orElseThrow(NoDataException::new);

        Comment comment = CreateCommentRequest.toEntity(folder, user, requestDto);

        return CreateCommentResponse.builder()
                .id(commentRepository.save(comment).getId())
                .build();
    }

    @Transactional
    public CommentListResponse getAllByFolder(Long folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(NoDataException::new);

        List<CommentResponse> list = commentRepository.findAllByFolder(folder).stream()
                .map(CommentResponse::fromEntity).collect(Collectors.toList());

        Boolean isPrivate = folder.getIsPrivate();

        return CommentListResponse.builder()
                .comments(list)
                .isPrivate(isPrivate)
                .build();
    }

    @Transactional
    public UpdateCommentResponse update(UpdateCommentRequest requestDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NoDataException::new);

        Folder folder = folderRepository.findById(requestDto.getFolderId())
                .orElseThrow(NoDataException::new);

        Comment comment = commentRepository.findByIdAndUser(requestDto.getId(), user)
                .orElseThrow(NoDataException::new);

        Comment updated = UpdateCommentRequest.toEntity(folder, user, comment);

        Comment save = commentRepository.save(updated);

        return UpdateCommentResponse.builder().id(save.getId()).build();
    }

    @Transactional
    public Long delete(Long id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NoDataException::new);

        Comment comment = commentRepository.findByIdAndUser(id, user)
                .orElseThrow(NoDataException::new);

        commentRepository.delete(comment);

        return comment.getId();
    }
}
