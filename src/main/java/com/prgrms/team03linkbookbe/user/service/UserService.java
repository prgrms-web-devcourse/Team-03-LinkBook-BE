package com.prgrms.team03linkbookbe.user.service;

import com.prgrms.team03linkbookbe.common.exception.NoDataException;
import com.prgrms.team03linkbookbe.interest.entity.Interest;
import com.prgrms.team03linkbookbe.interest.repository.InterestRepository;
import com.prgrms.team03linkbookbe.user.dto.MeResponseDto;
import com.prgrms.team03linkbookbe.user.dto.RegisterRequestDto;
import com.prgrms.team03linkbookbe.user.dto.UserUpdateRequestDto;
import com.prgrms.team03linkbookbe.user.dto.UserDetailResponseDto;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.exception.DuplicatedEmailException;
import com.prgrms.team03linkbookbe.user.exception.LoginFailureException;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final InterestRepository interestRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegisterRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicatedEmailException();
        }
        User user = requestDto.toEntity();
        user.encodePassword(passwordEncoder.encode(requestDto.getPassword()));
        User saveUser = userRepository.save(user);
    }

    public User login(String email, String credentials) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(LoginFailureException::new);
        user.checkPassword(passwordEncoder, credentials);
        return user;
    }

    @Transactional
    public void updateLastLoginAt(User user) {
        User findUser = userRepository.findById(user.getId())
            .orElseThrow(() -> new LoginFailureException());
        findUser.updateLastLoginAt(LocalDateTime.now());
    }

    public MeResponseDto me(String email) {
        return userRepository.findByEmailFetchJoinInterests(email)
            .map(MeResponseDto::fromEntity)
            .orElseThrow(NoDataException::new);
    }

    @Transactional
    public void updateUser(UserUpdateRequestDto requestDto, String email) {
        User user = userRepository.findByEmailFetchJoinInterests(email)
            .orElseThrow(() -> new NoDataException());
        List<Interest> interests = user.getInterests();
        for (Interest interest : interests) {
            interestRepository.delete(interest);
        }

        User updateUser = requestDto.toEntity();
        List<Interest> updateInterests = updateUser.getInterests();
        user.updateUser(updateUser.getName(), updateUser.getImage(), updateUser.getIntroduce(), updateUser.getInterests());
        for (Interest interest : updateInterests) {
            interest.changeUser(user);
            interestRepository.save(interest);
        }
    }

}
