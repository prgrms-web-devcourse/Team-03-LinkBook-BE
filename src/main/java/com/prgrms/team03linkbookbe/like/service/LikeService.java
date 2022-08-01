package com.prgrms.team03linkbookbe.like.service;

import com.prgrms.team03linkbookbe.common.exception.NoDataException;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import com.prgrms.team03linkbookbe.like.dto.CreateLikeRequestDto;
import com.prgrms.team03linkbookbe.like.dto.CreateLikeResponseDto;
import com.prgrms.team03linkbookbe.like.entity.Like;
import com.prgrms.team03linkbookbe.like.repository.LikeRepository;
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
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    @Transactional
    public CreateLikeResponseDto create(CreateLikeRequestDto requestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(NoDataException::new);

        Folder folder = folderRepository.findById(requestDto.getFolderId())
                .orElseThrow(NoDataException::new);

        Like like = CreateLikeRequestDto.toEntity(folder, user);

        return CreateLikeResponseDto.builder()
                .id(likeRepository.save(like).getId())
                .build();
    }

    @Transactional
    public Long delete(Long id, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(NoDataException::new);

        Like like = likeRepository.findByIdAndUser(id, user)
                .orElseThrow(NoDataException::new);

        likeRepository.delete(like);

        return like.getId();
    }
}
