package org.classmatechen.core.domain.platform.advertise.entity;

import java.time.LocalTime;

import org.classmatechen.core.domain.local.advertise.vo.LocalAdvertiseId;
import org.classmatechen.core.domain.platform.account.vo.AccountId;
import org.classmatechen.core.domain.platform.advertise.vo.AdvertiseId;
import org.classmatechen.core.domain.platform.advertise.vo.AdvertiseStatus;
import org.classmatechen.core.domain.platform.promotion.vo.PromotionId;

public class Advertise {

    private AdvertiseId id;
    private AccountId accountId;
    private PromotionId promotionId;
    private String name;
    private LocalAdvertiseId localAdvertiseId;
    private AdvertiseStatus status;
    private LocalTime operateTime;
    private LocalTime lastModifyTime;
    private LocalTime createTime;
}
