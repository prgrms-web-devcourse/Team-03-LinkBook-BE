package com.prgrms.team03linkbookbe.user.service;

import com.prgrms.team03linkbookbe.common.exception.NoDataException;
import com.prgrms.team03linkbookbe.user.dto.RegisterRequestDto;
import com.prgrms.team03linkbookbe.user.dto.RegisterResponseDto;
import com.prgrms.team03linkbookbe.user.dto.UpdateRequestDto;
import com.prgrms.team03linkbookbe.user.dto.UserResponseDto;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.exception.DuplicatedEmailException;
import com.prgrms.team03linkbookbe.user.exception.LoginFailureException;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User login(String email, String credentials) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new LoginFailureException());
        user.checkPassword(passwordEncoder, credentials);
        return user;
    }

    @Transactional
    public void register(RegisterRequestDto requestDto) {
        if(userRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicatedEmailException();
        }
        userRepository.save(requestDto.toEntity(passwordEncoder));
    }

    public UserResponseDto findByEmail(String email) {
        return userRepository.findByEmail(email)
            .map(user -> UserResponseDto.fromEntity(user))
            .orElseThrow(() -> new NoDataException());
    }

    @Transactional
    public void updateUser (UpdateRequestDto requestDto, String email) {
        User user = userRepository.findByEmailFetchJoinInterests(email)
            .orElseThrow(() -> new NoDataException());
        user.updateUser(requestDto.getName(), requestDto.getImage(), requestDto.getIntroduce(), requestDto.getInterests());
    }

}
