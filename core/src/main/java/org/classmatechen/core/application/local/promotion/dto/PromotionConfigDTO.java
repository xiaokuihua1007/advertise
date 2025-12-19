package org.classmatechen.core.application.local.promotion.dto;

import org.classmatechen.core.domain.local.promotion.vo.config.PromotionConfig;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PromotionConfigDTO {

    private Long localPromotionId;
    private PromotionConfig config;
}
