package org.classmatechen.core.infrastructure.third.impl;

import org.classmatechen.core.domain.local.promotion.vo.config.PromotionConfig;
import org.classmatechen.core.domain.platform.account.entity.Account;
import org.classmatechen.core.infrastructure.third.ThirdPartService;

public class ThirdPartServiceimpl implements ThirdPartService {

    private ClientFactory factory;

    public ThirdPartServiceimpl() {

        this.factory = new ClientFactory();
    }

    @Override
    public Long createPromotion(Account account, PromotionConfig config) {
        
        return this.factory.getClient(account.platform()).createPromotion(account.tokenId(), config);
    }
}
