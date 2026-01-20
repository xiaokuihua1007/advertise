package org.classmatechen.core.domain.share;

import org.classmatechen.core.domain.platform.account.entity.Account;
import org.classmatechen.core.domain.platform.account.repository.AccountRepository;
import org.classmatechen.core.domain.platform.account.vo.AccountId;
import org.classmatechen.core.domain.platform.promotion.entity.Promotion;
import org.classmatechen.core.domain.platform.promotion.repository.PromotionRepository;
import org.classmatechen.core.domain.share.third.AccountThird;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private AccountThird accountThird;

    public Long createPromotion(AccountId accountId, Object config) {

        Account account = repository.findById(accountId);
        if (!account.isRunning()) {
            throw new RuntimeException();
        }
        Long promotionId = accountThird.createPromotion(accountId, config);
        Promotion promotion = account.createPromotion(promotionId);

        promotionRepository.save(promotion);
        return promotionId;
    }
}
