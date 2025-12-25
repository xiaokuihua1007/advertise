package org.classmatechen.sample.po;

import lombok.Data;

@Data
public class Tencent {

    private Long clientId;
    private String clientSecret;
    private String authorizationCode;
    private String redirectUri;
    private String accessToken;
    private String refreshToken;
}
