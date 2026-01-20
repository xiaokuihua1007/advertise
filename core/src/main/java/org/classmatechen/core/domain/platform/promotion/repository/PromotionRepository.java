package org.classmatechen.core.domain.platform.promotion.repository;

import org.classmatechen.core.domain.platform.promotion.entity.Promotion;
import org.classmatechen.core.domain.platform.promotion.vo.PromotionId;

public interface PromotionRepository {

    Promotion findById(PromotionId promotionId);

    void save(Promotion promotion);

    void updateOperateTime(Promotion promotion);
}
