package com.prgrms.team03linkbookbe.refreshtoken.entity;

import com.prgrms.team03linkbookbe.common.entity.BaseDateEntity;
import com.prgrms.team03linkbookbe.user.entity.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false, columnDefinition = "varchar(500)")
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @Builder
    public RefreshToken(Long id, String token, User user) {
        this.id = id;
        this.token = token;
        this.user = user;
    }

    public void changeToken(String token) {
        this.token = token;
    }

}
