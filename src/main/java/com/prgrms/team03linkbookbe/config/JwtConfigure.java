package com.prgrms.team03linkbookbe.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter @Setter
@ToString
@ConfigurationProperties(prefix = "jwt")
public class JwtConfigure {

    private String accessHeader;

    private String refreshHeader;

    private int accessTokenExpirySeconds;

    private int RefreshTokenExpirySeconds;

    private String issuer;

    private String clientSecret;

}
