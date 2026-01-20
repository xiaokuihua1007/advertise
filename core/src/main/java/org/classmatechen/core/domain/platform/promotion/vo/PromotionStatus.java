package org.classmatechen.core.domain.platform.promotion.vo;

import org.classmatechen.core.common.CEnum;

public class PromotionStatus extends CEnum {

    /**
     * 正常
     */
    public static final PromotionStatus running = new PromotionStatus(0, "running");

    /**
     * 禁用
     */
    public static final PromotionStatus disable = new PromotionStatus(2, "disable");

    private PromotionStatus(int code, String lable) {
        super(code, lable);
    }
}
