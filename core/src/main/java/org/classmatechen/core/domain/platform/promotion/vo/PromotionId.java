package org.classmatechen.core.domain.platform.promotion.vo;

import java.util.Objects;

import org.classmatechen.common.Platform;

public class PromotionId {

    private final Long promotionId;
    private final Platform platform;

    public PromotionId(Long promotionId, Platform platform) {

        if (null == promotionId || promotionId <= 0 || null == platform) {
            throw new IllegalArgumentException();
        }
        this.promotionId = promotionId;
        this.platform = platform;
    }

    @Override
    public boolean equals(Object object) {

        if (null == object || !(object instanceof PromotionId)) {
            return false;
        }
        return Objects.equals(this.promotionId, ((PromotionId) object).promotionId) && Objects.equals(this.platform, ((PromotionId) object).platform);
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.promotionId, this.platform.getId());
    }

    public Platform platform() {

        return this.platform;
    }
}
