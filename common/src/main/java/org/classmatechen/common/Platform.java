package org.classmatechen.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Platform {

    Oceanengine(1, "巨量"),
    Tencent(2, "腾讯"),
    Baidu(4, "百度")
    ;

    private int id;
    private String name;

    public static Platform platform(int id) {

        for (Platform platform : Platform.values()) {
            if (platform.id == id) {
                return platform;
            }
        }
        throw new RuntimeException("unknown platform");
    }
}
