package org.classmatechen.core.domain.local.promotion.vo;

public record LocalPromotionId(Long id) {

    public LocalPromotionId {
        if (null == id || id <= 0) {
            throw new IllegalArgumentException("Long localPromotionId cannot be null when new LocalPromotionId()");
        }
    }
}
