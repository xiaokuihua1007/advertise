package org.classmatechen.core.domain.shared.service;

import org.classmatechen.core.domain.local.promotion.entity.LocalPromotion;
import org.classmatechen.core.domain.local.promotion.repository.LocalPromotionRepository;
import org.classmatechen.core.domain.local.promotion.vo.config.PromotionConfig;
import org.classmatechen.core.domain.platform.account.entity.Account;
import org.classmatechen.core.domain.platform.account.repository.AccountRepository;
import org.classmatechen.core.domain.platform.promotion.entity.Promotion;
import org.classmatechen.core.domain.platform.promotion.repository.PromotionRepository;
import org.classmatechen.core.domain.shared.third.ThirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocalPromotionDomainService {

    @Autowired
    private LocalPromotionRepository localPromotionRepository;

    @Autowired
    private AccountRepository accountRespository;

    @Autowired
    private ThirdService thirdService;

    @Autowired
    private PromotionRepository promotionRepository;

    @Transactional(rollbackFor = Exception.class)
    public void publishPlatformPromotion(Account account, LocalPromotion localPromotion, PromotionConfig config) {

        if (!account.isNormal() || localPromotion.hasCreatedWithPlatform(account.platform())) {
            throw new RuntimeException();
        }

        Long promotionId = thirdService.createPromotion(account, config);
        Promotion promotion = Promotion.create(promotionId, account.platform(), localPromotion.getId());
        account.addPromotion(promotion.id());
        localPromotion.addPromotion(account.platform(), promotion.id());

        this.promotionRepository.save(promotion);
        this.accountRespository.save(account);
        this.localPromotionRepository.save(localPromotion);

        // todo publish event
    }
}
