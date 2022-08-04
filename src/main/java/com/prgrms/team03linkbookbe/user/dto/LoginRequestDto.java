package com.prgrms.team03linkbookbe.user.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequestDto {

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "이메일 형식이 일치하지 않습니다.")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

}
