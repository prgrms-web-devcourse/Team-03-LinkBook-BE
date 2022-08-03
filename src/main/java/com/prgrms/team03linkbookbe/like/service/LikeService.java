package com.prgrms.team03linkbookbe.like.service;

import com.prgrms.team03linkbookbe.common.exception.NoDataException;
import com.prgrms.team03linkbookbe.folder.dto.FolderIdResponse;
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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    @Transactional
    public CreateLikeResponseDto create(CreateLikeRequestDto requestDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NoDataException::new);

        Folder folder = folderRepository.findById(requestDto.getFolderId())
                .orElseThrow(NoDataException::new);

        Like like = CreateLikeRequestDto.toEntity(folder, user);

        Long id = likeRepository.save(like).getId();

        folderRepository.save(folder.toBuilder()
                .likes(folder.getLikes() + 1)
                .build());

        return CreateLikeResponseDto.builder()
                .id(id)
                .build();
    }

    @Transactional
    public List<FolderIdResponse> getLikedFoldersByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(NoDataException::new);

        List<Like> likes = likeRepository.findAllByUser(user);

        return likes.stream().map(o ->
                        FolderIdResponse.fromEntity(o.getFolder().getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long delete(Long id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(NoDataException::new);

        Like like = likeRepository.findByIdAndUser(id, user)
                .orElseThrow(NoDataException::new);

        likeRepository.delete(like);

        folderRepository.save(like.getFolder().toBuilder()
                .likes(like.getFolder().getLikes() - 1)
                .build());

        return like.getId();
    }
}
