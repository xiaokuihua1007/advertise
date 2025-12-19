package org.classmatechen.core.infrastructure.third.impl.client;

import org.classmatechen.core.domain.local.promotion.vo.config.PromotionConfig;

public interface Client {

    Long createPromotion(String tokenId, PromotionConfig config);

    void updateAccount(String tokenId);
}
