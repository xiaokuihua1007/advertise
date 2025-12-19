package org.classmatechen.core.domain.platform.account.vo;

public enum AccountStatus {

    Noraml(0, "Normal"), // 正常状态
    ;

    private final int code;
    private final String lable;

    private AccountStatus(int code, String lable) {
        this.code = code;
        this.lable = lable;
    }
}
