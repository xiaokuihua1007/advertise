package org.classmatechen.core.domain.platform.promotion.entity;

import java.time.LocalDateTime;
import org.classmatechen.core.domain.platform.account.vo.AccountId;
import org.classmatechen.core.domain.platform.promotion.vo.PromotionId;
import org.classmatechen.core.domain.platform.promotion.vo.PromotionStatus;

public class Promotion {

    private PromotionId id;
    private AccountId accountId;
    private String name;
    private PromotionStatus status;
    private LocalDateTime operateTime;
    private LocalDateTime lastModifyTime;
    private LocalDateTime createTime;

    public boolean isRunning() {
        return PromotionStatus.running == this.status;
    }

    public boolean canPublish() {
        return PromotionStatus.running == this.status;
    }

    public void refreshOperateTime() {
        this.operateTime = LocalDateTime.now();
    }
}
