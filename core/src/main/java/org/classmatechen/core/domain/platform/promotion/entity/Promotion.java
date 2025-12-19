package org.classmatechen.core.domain.platform.promotion.entity;

import java.time.LocalTime;
import java.util.List;

import org.classmatechen.common.Platform;
import org.classmatechen.core.domain.local.promotion.vo.LocalPromotionId;
import org.classmatechen.core.domain.platform.account.vo.AccountId;
import org.classmatechen.core.domain.platform.advertise.vo.AdvertiseId;
import org.classmatechen.core.domain.platform.promotion.vo.PromotionId;
import org.classmatechen.core.domain.platform.promotion.vo.PromotionStatus;

public class Promotion {

    private PromotionId id;
    private LocalPromotionId localPromotionId;
    private AccountId accountId;
    private String name;
    private PromotionStatus status;
    private LocalTime operateTime;
    private LocalTime lastModifyTime;
    private LocalTime createTime;
    private List<AdvertiseId> advertiseIds;

    public static Promotion create(Long promotionId, Platform platform, LocalPromotionId localPromotionId) {
        Promotion promotion = new Promotion();
        promotion.id = new PromotionId(promotionId, platform);
        promotion.localPromotionId = localPromotionId;
        promotion.operateTime = LocalTime.now();
        return promotion;
    }

    public PromotionId id() {
        return this.id;
    }
}
