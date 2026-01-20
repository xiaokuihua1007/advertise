package org.classmatechen.core.domain.local.promotion.entity;

import java.util.Map;

import org.classmatechen.common.Platform;
import org.classmatechen.core.domain.local.promotion.vo.LocalPromotionId;
import org.classmatechen.core.domain.local.promotion.vo.PromotionRef;
import org.classmatechen.core.domain.local.promotion.vo.config.DyPromotionConfig;
import org.classmatechen.core.domain.local.promotion.vo.config.LocalPromotionConfig;
import org.classmatechen.core.domain.local.promotion.vo.config.PromotionConfig;

/**
 * 本地推广
 */
public class LocalPromotion {

    private LocalPromotionId id;
    private LocalPromotionConfig config;
    private Map<Platform, PromotionRef> promotions;

    public PromotionConfig generate(Platform platform) {

        PromotionConfig config;
        switch (platform) {
            case Oceanengine:
                config = new DyPromotionConfig();
                break;
            default:
                config = null;
                break;
        }
        if (null != config) {
            config.init(this.config);
        }
        return config;
    }

    public void checkBeforeAddPromotion(Platform platform) {

        if (this.promotions.containsKey(platform)) {
            throw new RuntimeException();
        }
    }

    public void addPromotion(Platform platform, PromotionRef ref) {

        this.checkBeforeAddPromotion(platform);
        this.promotions.put(platform, ref);
    }
}
