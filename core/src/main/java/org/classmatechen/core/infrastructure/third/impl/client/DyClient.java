package org.classmatechen.core.infrastructure.third.impl.client;

import org.classmatechen.core.domain.local.promotion.vo.config.PromotionConfig;
import org.classmatechen.core.domain.local.promotion.vo.config.impl.DyPromotionConfig;
import org.classmatechen.oceanengine.DyContextImpl;
import org.classmatechen.oceanengine.request.ProjectCreatePost;

public class DyClient implements Client {

    @Override
    public Long createPromotion(String tokenId, PromotionConfig config) {

        if (!(config instanceof DyPromotionConfig)) {
            throw new RuntimeException();
        }
        return new ProjectCreatePost().request(new DyContextImpl(Long.parseLong(tokenId)), (DyPromotionConfig) config).getData();
    }

    @Override
    public void updateAccount(String tokenId) {
    }
}
