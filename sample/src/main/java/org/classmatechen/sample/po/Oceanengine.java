package org.classmatechen.sample.po;

import lombok.Data;

@Data
public class Oceanengine {

    private Long appId;
    private String secret;
    private String authCode;
    private String refreshToken;
    private String accessToken;
}
