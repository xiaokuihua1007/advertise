package org.classmatechen.core.domain.local.promotion.error;

import org.classmatechen.core.domain.local.promotion.vo.LocalPromotionId;

public class LocalPromotionNoExistException extends RuntimeException {

    public LocalPromotionNoExistException(LocalPromotionId id) {
        super("no exist account, id:" + id.id());
    }
}
