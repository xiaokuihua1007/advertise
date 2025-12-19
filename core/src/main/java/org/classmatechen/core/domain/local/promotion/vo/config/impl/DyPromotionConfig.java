package org.classmatechen.core.domain.local.promotion.vo.config.impl;

import java.util.Objects;

import org.classmatechen.core.domain.local.promotion.vo.config.PromotionConfig;
import org.classmatechen.core.domain.local.promotion.vo.config.PromotionType;

import com.bytedance.ads.model.ProjectCreateV30AdType;
import com.bytedance.ads.model.ProjectCreateV30Request;

public class DyPromotionConfig extends ProjectCreateV30Request implements PromotionConfig {

    private DyPromotionConfig() { }

    public static DyPromotionConfig from(LocalPromotionConfig source) {

        DyPromotionConfig config = new DyPromotionConfig();
        String name = source.getName();
        config.setName(name);
        PromotionType type = source.getType();
        if (Objects.nonNull(type)) {
            switch (type) {
                case Normal:
                    config.setAdType(ProjectCreateV30AdType.ALL);
                    break;
                case Serach:
                    config.setAdType(ProjectCreateV30AdType.SEARCH);
                    break;
                default:
                    break;
            }
        }
        return config;
    }

    @Override
    public void check() { }
}
