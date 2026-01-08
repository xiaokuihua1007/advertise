package org.classmatechen.core.domain.platform.promotion.vo;

import org.classmatechen.common.Platform;

public record PromotionId(Long promotionId, Platform platform) {

    public PromotionId {

        if (null == promotionId || promotionId <= 0 || null == platform) {
            throw new IllegalArgumentException();
        }
    }
}
