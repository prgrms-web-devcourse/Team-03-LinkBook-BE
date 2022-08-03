package com.prgrms.team03linkbookbe.user.entity;

import com.prgrms.team03linkbookbe.interest.entity.Interest;
import com.prgrms.team03linkbookbe.user.exception.LoginFailureException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(20)")
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "introduce", columnDefinition = "varchar(50)")
    private String introduce;

    @Column(name = "role", nullable = false, columnDefinition = "varchar(20)")
    private String role;

    @OneToMany(mappedBy = "user")
    private List<Interest> interests = new ArrayList<>();

    @Builder
    public User(Long id, String email, String password, String name, String image, String introduce,
        String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.image = image;
        this.introduce = introduce;
        this.role = role;
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

    public void updateUser(String name, String image, String introduce, List<Interest> interests) {
        this.name = name;
        this.image = image;
        this.introduce = introduce;
        this.interests = interests;
    }

}
