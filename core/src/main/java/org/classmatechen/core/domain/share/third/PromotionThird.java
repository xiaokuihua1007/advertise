package org.classmatechen.core.domain.share.third;

import org.classmatechen.core.domain.platform.promotion.vo.PromotionId;

public interface PromotionThird {

    void pause(PromotionId promotionId);

    void publish(PromotionId promotionId);
}
