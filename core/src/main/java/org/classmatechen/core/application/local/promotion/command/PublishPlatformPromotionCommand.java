package org.classmatechen.core.application.local.promotion.command;

import org.classmatechen.core.domain.local.promotion.vo.config.PromotionConfig;

import lombok.Data;

@Data
public class PublishPlatformPromotionCommand {

    private Long accountId;
    private Integer platformId;
    private Long localPromotionId;
    private PromotionConfig config;
}
