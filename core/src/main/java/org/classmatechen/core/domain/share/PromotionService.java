package org.classmatechen.core.domain.share;

import org.classmatechen.core.domain.platform.promotion.entity.Promotion;
import org.classmatechen.core.domain.platform.promotion.repository.PromotionRepository;
import org.classmatechen.core.domain.platform.promotion.vo.PromotionId;
import org.classmatechen.core.domain.share.third.PromotionThird;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository repository;

    @Autowired
    private PromotionThird third;

    public void pause(PromotionId promotionId) {

        Promotion promotion = repository.findById(promotionId);
        if (null == promotion) {

        }
        if (!promotion.isRunning()) {

        }
        third.pause(promotionId);
        promotion.refreshOperateTime();

        repository.updateOperateTime(promotion);
    }

    public void publish(PromotionId promotionId) {

        Promotion promotion = repository.findById(promotionId);
        if (null == promotion) {

        }
        if (!promotion.canPublish()) {

        }
        third.publish(promotionId);
        promotion.refreshOperateTime();

        repository.updateOperateTime(promotion);
    }
}
