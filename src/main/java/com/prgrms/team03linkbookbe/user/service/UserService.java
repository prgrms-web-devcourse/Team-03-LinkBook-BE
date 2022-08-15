package com.prgrms.team03linkbookbe.user.service;

import com.prgrms.team03linkbookbe.common.exception.NoDataException;
import com.prgrms.team03linkbookbe.email.service.EmailService;
import com.prgrms.team03linkbookbe.interest.service.InterestService;
import com.prgrms.team03linkbookbe.user.dto.MeResponseDto;
import com.prgrms.team03linkbookbe.user.dto.RegisterRequestDto;
import com.prgrms.team03linkbookbe.user.dto.UserUpdateRequestDto;
import com.prgrms.team03linkbookbe.user.entity.User;
import com.prgrms.team03linkbookbe.user.exception.DuplicatedEmailException;
import com.prgrms.team03linkbookbe.user.exception.IllegalPasswordException;
import com.prgrms.team03linkbookbe.user.exception.LoginFailureException;
import com.prgrms.team03linkbookbe.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.regex.Pattern;
import javax.servlet.http.HttpSession;
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
    private final InterestService interestService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public void register(HttpSession httpSession, RegisterRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicatedEmailException();
        }
        User user = requestDto.toEntity();

        String password = requestDto.getPassword();
        if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", password)) {
            throw new IllegalPasswordException();
        }

//        emailService.IsCertificatedEmail(httpSession, user.getEmail());
        user.encodePassword(passwordEncoder.encode(requestDto.getPassword()));
        userRepository.save(user);
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

        User updateUser = requestDto.toEntity();
        user.updateUser(updateUser.getName(), updateUser.getImage(), updateUser.getIntroduce());
        interestService.updateInterests(requestDto.getInterests(), user);
    }

}
