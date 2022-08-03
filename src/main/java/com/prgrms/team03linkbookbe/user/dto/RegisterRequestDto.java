package com.prgrms.team03linkbookbe.user.dto;

import com.prgrms.team03linkbookbe.user.entity.User;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegisterRequestDto {

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "이메일 형식이 일치하지 않습니다.")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "Image URL cannot be blank")
    private String image;

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
            .email(this.getEmail())
            .password(passwordEncoder.encode(password))
            .image(image)
            .name("익명의사용자")
            .role("ROLE_USER")
            .build();
    }

}