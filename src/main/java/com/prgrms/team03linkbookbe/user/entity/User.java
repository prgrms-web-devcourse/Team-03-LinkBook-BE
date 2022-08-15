package com.prgrms.team03linkbookbe.user.entity;

import com.prgrms.team03linkbookbe.common.entity.BaseDateEntity;
import com.prgrms.team03linkbookbe.interest.entity.Interest;
import com.prgrms.team03linkbookbe.user.exception.LoginFailureException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

@DynamicInsert
@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일을 입력해주세요.")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Length(min = 1, max = 8, message = "닉네임을 1자 이상 8자 이하로 입력해주세요.")
    @Column(name = "name", nullable = false, columnDefinition = "varchar(20)")
    private String name;

    @NotBlank(message = "이미지 URL을 입력해주세요.")
    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "introduce", columnDefinition = "varchar(50)")
    private String introduce;

    @NotBlank(message = "권한을 입력해주세요.")
    @Column(name = "role", nullable = false, columnDefinition = "varchar(20)")
    private String role;

    @Column(name = "last_login")
    private LocalDateTime lastLoginAt;

    @OneToMany(mappedBy = "user")
    private List<Interest> interests = new ArrayList<>();

    @PrePersist
    public void perPersist() {
        this.name = this.getName() == null ? "익명의사용자" : this.getName();
        this.role = this.getRole() == null ? "ROLE_USER" : this.getRole();
    }

    @Builder
    public User(Long id, String email, String password, String name, String image, String introduce,
        String role, LocalDateTime lastLoginAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.image = image;
        this.introduce = introduce;
        this.role = role;
        this.lastLoginAt = lastLoginAt;
    }

    public void checkPassword(PasswordEncoder passwordEncoder, String credentials) {
        if (!passwordEncoder.matches(credentials, password)) {
            throw new LoginFailureException();
        }
    }

    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> roles = new ArrayList<>();
        for (String role : this.role.split(",")) {
            roles.add(new SimpleGrantedAuthority(role));
        }
        return roles;
    }

    public void addInterest(Interest interest) {
        this.interests.add(interest);
        if (interest.getUser() != this) {
            interest.changeUser(this);
        }
    }

    public void updateLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public void updateUser(String name, String image, String introduce) {
        this.name = name;
        this.image = image;
        this.introduce = introduce;
    }

    public void encodePassword(String password) {
        this.password = password;
    }

}
