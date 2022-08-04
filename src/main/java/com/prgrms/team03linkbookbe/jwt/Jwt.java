package com.prgrms.team03linkbookbe.jwt;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.ToString;

@ToString
public class Jwt {

    private final String issuer;
    private final String clientSecret;
    private final int accessTokenExpirySeconds;
    private final int refreshTokenExpirySeconds;
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    public Jwt(String issuer, String clientSecret, int accessTokenExpirySeconds, int refreshTokenExpirySeconds) {
        this.issuer = issuer;
        this.clientSecret = clientSecret;
        this.accessTokenExpirySeconds = accessTokenExpirySeconds;
        this.refreshTokenExpirySeconds = refreshTokenExpirySeconds;
        this.algorithm = Algorithm.HMAC512(clientSecret);
        this.jwtVerifier = com.auth0.jwt.JWT.require(algorithm)
            .withIssuer(issuer)
            .build();
    }

    public String createAccessToken (Claims claims) {
        Date now = new Date();
        JWTCreator.Builder builder = com.auth0.jwt.JWT.create();
        builder.withIssuer(issuer);
        builder.withIssuedAt(now);
        if (accessTokenExpirySeconds > 0) {
            builder.withExpiresAt(new Date(now.getTime() + accessTokenExpirySeconds * 1_000L));
        }
        builder.withClaim("email", claims.email);
        builder.withArrayClaim("roles", claims.roles);
        return builder.sign(algorithm);
    }

    public String createRefreshToken (Claims claims) {
        Date now = new Date();
        JWTCreator.Builder builder = com.auth0.jwt.JWT.create();
        builder.withIssuer(issuer);
        builder.withIssuedAt(now);
        if (refreshTokenExpirySeconds > 0) {
            builder.withExpiresAt(new Date(now.getTime() + refreshTokenExpirySeconds * 1_000L));
        }
        builder.withClaim("email", claims.email);
        builder.withArrayClaim("roles", claims.roles);
        return builder.sign(algorithm);
    }

    public Claims verify(String token) {
        return new Claims(jwtVerifier.verify(token));
    }

    public Boolean isExpiredToken(String accessToken) {
        DecodedJWT decodedJWT = com.auth0.jwt.JWT.decode(accessToken);
        return decodedJWT.getExpiresAt().after(new Date());
    }

    static public class Claims {
        private String email;
        private String[] roles;
        private Date iat;
        private Date exp;

        private Claims() {}

        Claims(DecodedJWT decodedJWT) {
            Claim email = decodedJWT.getClaim("email");
            if(!email.isNull()) {
                this.email = email.asString();
            }
            Claim roles = decodedJWT.getClaim("roles");
            if(!roles.isNull()) {
                this.roles = roles.asArray(String.class);
            }
            this.iat = decodedJWT.getIssuedAt();
            this.exp = decodedJWT.getExpiresAt();
        }

        public static Claims from(String email, String[] roles) {
            Claims claims = new Claims();
            claims.email = email;
            claims.roles = roles;
            return claims;
        }

        public Map<String, Object> asMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("email", email);
            map.put("roles", roles);
            map.put("iat", iat());
            map.put("exp", exp());
            return map;
        }

        public long iat() {
            return iat != null ? iat.getTime() : -1;
        }

        public long exp() {
            return exp != null ? exp.getTime() : -1;
        }

        public String getEmail() {
            return email;
        }

        public String[] getRoles() {
            return roles;
        }

        public Date getIat() {
            return iat;
        }

        public Date getExp() {
            return exp;
        }
    }

}
