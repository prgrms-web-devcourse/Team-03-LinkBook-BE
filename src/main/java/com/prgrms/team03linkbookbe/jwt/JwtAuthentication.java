package com.prgrms.team03linkbookbe.jwt;

import lombok.ToString;

@ToString
public class JwtAuthentication {

    public final String accessToken;

    public final String refreshToken;

    public final String email;

    public JwtAuthentication(String accessToken, String refreshToken, String email) {
        if(accessToken.isEmpty()) {
            throw new IllegalArgumentException("token must be provided");
        }
        if(email.isEmpty()) {
            throw new IllegalArgumentException("email must be provided");
        }
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.email = email;
    }

}
