package com.prgrms.team03linkbookbe.like.service;

import com.prgrms.team03linkbookbe.folder.dto.FolderListResponse;
import com.prgrms.team03linkbookbe.folder.entity.Folder;
import com.prgrms.team03linkbookbe.folder.repository.FolderRepository;
import com.prgrms.team03linkbookbe.like.dto.CreateLikeRequest;
import com.prgrms.team03linkbookbe.like.dto.CreateLikeResponse;
import com.prgrms.team03linkbookbe.like.entity.Like;
import com.prgrms.team03linkbookbe.like.repository.LikeRepository;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
    public CreateLikeResponse create(CreateLikeRequest requestDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

        Folder folder = folderRepository.findById(requestDto.getFolderId())
                .orElseThrow(() -> new IllegalArgumentException("해당 폴더는 존재하지 않습니다."));

        if (Objects.equals(user.getId(), folder.getUser().getId())) {
            throw new IllegalArgumentException("자신의 폴더에 좋아요를 할 수 없습니다.");
        }

        Like like = CreateLikeRequest.toEntity(folder, user);

        Long id = likeRepository.save(like).getId();

        folderRepository.save(folder.toBuilder()
                .likes(likeRepository.countByFolderEquals(folder))
                .build());

        return CreateLikeResponse.builder()
                .id(id)
                .build();
    }

    @Transactional
    public FolderListResponse getLikedFoldersByUserId(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

        List<Like> likes = likeRepository.findAllByUser(user);

        List<Folder> list = likes.stream().map(Like::getFolder)
                .collect(Collectors.toList());

        Page<Folder> folders = new PageImpl<>(list, pageable, list.size());

        return FolderListResponse.fromEntity(folders, likes);
    }

    @Transactional
    public Long delete(Long folderId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

        Folder folder = folderRepository.findById(folderId).orElseThrow(
                () -> new IllegalArgumentException("해당 폴더는 존재하지 않습니다."));

        Like like = likeRepository.findByFolderAndUser(folder, user)
                .orElseThrow(() -> new IllegalArgumentException("해당 좋아요는 존재하지 않습니다."));

        if (!Objects.equals(user.getId(), like.getUser().getId())) {
            throw new AccessDeniedException("자신의 좋아요만 삭제 가능합니다.");
        }

        likeRepository.delete(like);

        folderRepository.save(like.getFolder().toBuilder()
                .likes(likeRepository.countByFolderEquals(like.getFolder()))
                .build());

        return like.getId();
    }
}
