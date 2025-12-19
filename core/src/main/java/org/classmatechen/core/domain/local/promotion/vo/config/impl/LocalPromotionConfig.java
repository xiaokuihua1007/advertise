package org.classmatechen.core.domain.local.promotion.vo.config.impl;

import org.classmatechen.core.domain.local.promotion.vo.config.PromotionConfig;
import org.classmatechen.core.domain.local.promotion.vo.config.PromotionType;

import lombok.Data;

@Data
public class LocalPromotionConfig implements PromotionConfig {

    private String name;
    private PromotionType type;

    @Override
    public void check() { }
}
