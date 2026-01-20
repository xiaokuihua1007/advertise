package org.classmatechen.core.domain.platform.account.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.classmatechen.common.Platform;
import org.classmatechen.core.domain.local.promotion.vo.LocalPromotionId;
import org.classmatechen.core.domain.platform.account.vo.AccountId;
import org.classmatechen.core.domain.platform.account.vo.AccountStatus;
import org.classmatechen.core.domain.platform.promotion.entity.Promotion;
import org.classmatechen.core.domain.platform.promotion.vo.PromotionId;

/**
 * 账户
 * 只读
 * 只能在广告平台创建
 */
public class Account {

    private AccountId id;
    private Platform platform;
    private String name;
    private String tokenId;
    private AccountStatus status;
    private BigDecimal balance;
    private LocalDateTime createTime;
    private Long staffId;
    private List<PromotionId> promotionIds;

    public boolean isRunning() {
        return AccountStatus.RUNNING == this.status;
    }

    public Platform platform() {
        return this.platform;
    }

    /**
     * 创建推广
     * @param promotionId
     * @param localPromotionId
     * @return
     */
    public Promotion createPromotion(Long promotionId, LocalPromotionId localPromotionId) {

        if (isRunning()) {
            this.promotionIds.add(new PromotionId(promotionId, platform));
        }
        Promotion promotion = new Promotion();
        return promotion;
    }
}
