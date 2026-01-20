package org.classmatechen.core.domain.local.promotion.vo;

public record LocalPromotionId(Long localPromotionId) {

    public LocalPromotionId {

        if (null == localPromotionId || localPromotionId <= 0) {
            throw new IllegalArgumentException();
        }
    }
}
