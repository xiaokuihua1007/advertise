package org.classmatechen.core.domain.local.promotion.vo;

import org.classmatechen.common.Platform;
import org.classmatechen.core.domain.platform.promotion.vo.PromotionId;

import lombok.Getter;

@Getter
public class PromotionRef {

    private final PromotionId promotionId;
    private final Platform platform;

    public PromotionRef(
        PromotionId promotionId,
        Platform platform
    ) {
        this.promotionId = promotionId;
        this.platform = platform;
    }
}
