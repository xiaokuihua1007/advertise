package org.classmatechen.core.domain.local.promotion.vo;

import java.util.Objects;

public class LocalPromotionId {

    private final Long localPromotionId;

    public LocalPromotionId(Long localPromotionId) {

        if (Objects.isNull(localPromotionId)) {
            throw new IllegalArgumentException("Long localPromotionId cannot be null when new LocalPromotionId()");
        }
        this.localPromotionId = localPromotionId;
    }

    @Override
    public boolean equals(Object object) {

        if (Objects.isNull(object) || !(object instanceof LocalPromotionId)) {
            return false;
        }
        return ((LocalPromotionId) object).localPromotionId == this.localPromotionId;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.localPromotionId);
    }

    public Long id() {
        return this.localPromotionId;
    }
}
