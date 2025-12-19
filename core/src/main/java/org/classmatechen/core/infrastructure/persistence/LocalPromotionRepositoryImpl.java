package org.classmatechen.core.infrastructure.persistence;

import org.classmatechen.core.domain.local.promotion.entity.LocalPromotion;
import org.classmatechen.core.domain.local.promotion.repository.LocalPromotionRepository;
import org.classmatechen.core.domain.local.promotion.vo.LocalPromotionId;
import org.classmatechen.core.mapper.LocalPromotionPoMapper;
import org.classmatechen.core.po.LocalPromotionPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LocalPromotionRepositoryImpl implements LocalPromotionRepository {

    @Autowired
    private LocalPromotionPoMapper mapper;

    public LocalPromotion findById(LocalPromotionId localPromotionId) {
        LocalPromotionPo po = mapper.findById(localPromotionId.id());
        return null;
    }

    @Override
    public void save(LocalPromotion promotion) {

    }
}
