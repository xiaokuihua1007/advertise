package org.classmatechen.core.domain.platform.account.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.classmatechen.common.Platform;
import org.classmatechen.core.domain.platform.account.error.PromotionAddNotAllow;
import org.classmatechen.core.domain.platform.account.vo.AccountId;
import org.classmatechen.core.domain.platform.account.vo.AccountStatus;
import org.classmatechen.core.domain.platform.promotion.vo.PromotionId;

public class Account {

    private AccountId id;
    private String name;
    private String tokenId;
    private AccountStatus status;
    private BigDecimal balance;
    private LocalDateTime createTime;
    private Long userId;
    private List<PromotionId> promotionIds;

    public Platform platform() {
        return this.id.platform();
    }

    public String tokenId() {
        return this.tokenId;
    }

    public boolean isNormal() {

        return this.status == AccountStatus.Noraml;
    }

    public void addPromotion(PromotionId promotionId) {

        if (!isNormal()) {
            throw new PromotionAddNotAllow();
        }
        this.promotionIds.add(promotionId);
    }
}
