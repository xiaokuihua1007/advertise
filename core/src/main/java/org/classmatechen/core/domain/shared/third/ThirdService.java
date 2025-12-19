package org.classmatechen.core.domain.shared.third;

import org.classmatechen.core.domain.local.promotion.vo.config.PromotionConfig;
import org.classmatechen.core.domain.platform.account.entity.Account;

public interface ThirdService {

    Long createPromotion(Account account, PromotionConfig config);
}
