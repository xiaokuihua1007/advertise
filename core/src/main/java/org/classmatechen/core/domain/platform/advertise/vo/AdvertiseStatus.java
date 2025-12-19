package org.classmatechen.core.domain.platform.advertise.vo;

public enum AdvertiseStatus {

    Noraml(0, "Normal"), // 正常状态
    ;

    private final int code;
    private final String lable;

    private AdvertiseStatus(int code, String lable) {
        this.code = code;
        this.lable = lable;
    }
}
