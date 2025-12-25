package org.classmatechen.sample.po;

import lombok.Data;

@Data
public class Baidu {

    private Long userId;
    private String appId;
    private String authCode;
    private String secretKey;
    private String accessToken;
    private String refreshToken;
}
