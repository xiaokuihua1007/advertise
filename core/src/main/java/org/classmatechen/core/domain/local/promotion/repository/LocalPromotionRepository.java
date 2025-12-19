package org.classmatechen.core.domain.local.promotion.repository;

import org.classmatechen.core.domain.local.promotion.entity.LocalPromotion;
import org.classmatechen.core.domain.local.promotion.vo.LocalPromotionId;

public interface LocalPromotionRepository {

    LocalPromotion findById(LocalPromotionId id);

    void save(LocalPromotion promotion);
}
