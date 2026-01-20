package org.classmatechen.core.domain.share.third;

import org.classmatechen.core.domain.platform.account.vo.AccountId;

public interface AccountThird {

    Long createPromotion(AccountId accountId, Object object);
}
