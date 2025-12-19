package org.classmatechen.core.domain.platform.promotion.vo;

public enum PromotionStatus {

    Noraml(0, "Normal"), // 正常状态
    Nuknown(1, "Nuknown"),
    ;

    private final int code;
    private final String lable;

    private PromotionStatus(int code, String lable) {
        this.code = code;
        this.lable = lable;
    }
}
