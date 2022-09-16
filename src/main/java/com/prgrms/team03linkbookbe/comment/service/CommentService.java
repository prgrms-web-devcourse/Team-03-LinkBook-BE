package com.prgrms.team03linkbookbe.comment.service;

import com.prgrms.team03linkbookbe.comment.dto.CommentListResponse;
import com.prgrms.team03linkbookbe.comment.dto.CommentResponse;
import com.prgrms.team03linkbookbe.comment.dto.CreateCommentRequest;
import com.prgrms.team03linkbookbe.comment.dto.CreateCommentResponse;
import com.prgrms.team03linkbookbe.comment.dto.UpdateCommentRequest;
import com.prgrms.team03linkbookbe.comment.dto.UpdateCommentResponse;
import com.prgrms.team03linkbookbe.comment.entity.Comment;
import com.prgrms.team03linkbookbe.comment.repository.CommentRepository;
import com.prgrms.team03linkbookbe.common.exception.NoDataException;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
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
    public CreateCommentResponse create(CreateCommentRequest requestDto, String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(NoDataException::new);

        Folder folder = folderRepository.findById(requestDto.getFolderId())
            .orElseThrow(NoDataException::new);

        Comment comment;

        if (requestDto.getParentId() != null) {
            Comment parent = commentRepository.findById(requestDto.getParentId())
                .orElseThrow(NoDataException::new);
            comment = CreateCommentRequest.toEntity(folder, user, parent, requestDto);
        } else {
            comment = CreateCommentRequest.toEntity(folder, user, requestDto);
        }

        return CreateCommentResponse.builder()
            .id(commentRepository.save(comment).getId())
            .build();
    }

    @Transactional
    public CommentListResponse getAllByFolder(Long folderId) {
        Folder folder = folderRepository.findById(folderId)
            .orElseThrow(NoDataException::new);

        List<CommentResponse> list = commentRepository.findAllParentsByFolder(folder).stream()
            .map(CommentResponse::fromEntity).collect(Collectors.toList());

        return CommentListResponse.builder()
            .comments(list)
            .build();
    }

    @Transactional
    public UpdateCommentResponse update(Long id, UpdateCommentRequest requestDto, String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(NoDataException::new);

        Comment comment = commentRepository.findByIdAndUser(requestDto.getId(), user)
            .orElseThrow(NoDataException::new);

        if (!Objects.equals(user.getId(), comment.getUser().getId())) {
            throw new AccessDeniedException("자신의 코멘트만 수정 가능합니다.");
        }

        Comment byId = commentRepository.getReferenceById(id).toBuilder()
            .content(requestDto.getContent())
            .build();

        commentRepository.save(byId);

        return UpdateCommentResponse.builder().id(byId.getId()).build();
    }

    @Transactional
    public Long delete(Long id, String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(NoDataException::new);

        Comment comment = commentRepository.findByIdAndUser(id, user)
            .orElseThrow(NoDataException::new);

        if (!Objects.equals(user.getId(), comment.getUser().getId())) {
            throw new AccessDeniedException("자신의 코멘트만 삭제 가능합니다.");
        }

        commentRepository.delete(comment);

        return comment.getId();
    }
}
