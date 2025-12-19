package org.classmatechen.core.domain.local.promotion.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.classmatechen.common.Platform;
import org.classmatechen.core.domain.local.promotion.vo.LocalPromotionId;
import org.classmatechen.core.domain.local.promotion.vo.PromotionRef;
import org.classmatechen.core.domain.local.promotion.vo.config.PromotionConfig;
import org.classmatechen.core.domain.local.promotion.vo.config.impl.DyPromotionConfig;
import org.classmatechen.core.domain.local.promotion.vo.config.impl.LocalPromotionConfig;
import org.classmatechen.core.domain.platform.promotion.vo.PromotionId;

import lombok.Getter;

/**
 * 本地推广
 * 持有根据本地推广创建的各个平台的推广引用
 * 持有本地推广的配置
 */
public class LocalPromotion {

    @Getter
    private final LocalPromotionId id;
    private final Map<Platform, PromotionRef> promotions;
    private final LocalPromotionConfig config;

    public LocalPromotion(
        Long id,
        Map<Platform, PromotionRef> promotions,
        LocalPromotionConfig config
    ) {
        this.id = new LocalPromotionId(id);
        this.promotions = Objects.isNull(promotions) ? new HashMap<>() : promotions;
        if (Objects.isNull(config)) {
            throw new IllegalArgumentException();
        }
        this.config = config;
    }

    public boolean createdWithPlatform(Platform platform) {

        return this.promotions.containsKey(platform);
    }

    public void addPromotion(Platform platform, PromotionId promotionId) {

        if (createdWithPlatform(platform)) {
            throw new RuntimeException();
        }
        this.promotions.put(platform, new PromotionRef(promotionId, platform));
    }
    
    /**
     * 生成某个平台的配置
     * @param platform
     * @return
     */
    public PromotionConfig generatePlatformConfig(Platform platform) {

        PromotionConfig config;
        switch (platform) {
            case Oceanengine:
                config = DyPromotionConfig.from(this.config);
                break;
            default:
                config = null;
                break;
        }
        if (Objects.isNull(config)) {
            throw new RuntimeException("不支持该平台:" + platform.getName());
        }
        return config;
    }
}
