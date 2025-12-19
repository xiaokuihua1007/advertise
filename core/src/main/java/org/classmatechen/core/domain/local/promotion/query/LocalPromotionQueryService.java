package org.classmatechen.core.domain.local.promotion.query;

import org.classmatechen.core.mapper.LocalPromotionPoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocalPromotionQueryService {

    @Autowired
    private LocalPromotionPoMapper mapper;
}
