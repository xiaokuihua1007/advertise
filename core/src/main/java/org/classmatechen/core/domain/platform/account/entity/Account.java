package org.classmatechen.core.domain.platform.account.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private Long staffId;
    private List<PromotionId> promotionIds;

    private Account() { }

    public static Account factory(
        AccountId id,
        String name,
        String tokenId,
        AccountStatus status,
        BigDecimal balance,
        LocalDateTime createTime,
        Long staffId,
        List<PromotionId> promotionIds
    ) {

        Account account = new Account();
        account.setId(id);
        account.setName(name);
        account.setTokenId(tokenId);
        account.setStatus(status);
        account.setBalance(balance);
        account.setCreateTime(createTime);
        account.setStaffId(staffId);
        account.setPromotionIds(promotionIds);
        return account;
    }

    private void setId(AccountId id) {
        if (null == id) {
            throw new IllegalArgumentException();
        }
        this.id = id;
    }

    private void setName(String name) {
        if (null == name || name.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    private void setTokenId(String tokenId) {
        if (null == tokenId || tokenId.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.tokenId = tokenId;
    }

    private void setStatus(AccountStatus status) {
        if (null == status) {
            throw new IllegalArgumentException();
        }
        this.status = status;
    }

    private void setBalance(BigDecimal balance) {
        if (null == balance) {
            this.balance = BigDecimal.ZERO;
        } else {
            this.balance = balance;
        }
    }

    private void setCreateTime(LocalDateTime createTime) {
        if (null == createTime) {
            throw new IllegalArgumentException();
        }
        this.createTime = createTime;
    }

    private void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    private void setPromotionIds(List<PromotionId> promotionIds) {
        if (null == promotionIds) {
            this.promotionIds = new ArrayList<>();
        } else {
            this.promotionIds = promotionIds;
        }
    }

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
