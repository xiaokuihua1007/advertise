package org.classmatechen.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.classmatechen.core.po.LocalPromotionPo;

@Mapper
public interface LocalPromotionPoMapper {

    LocalPromotionPo findById(Long id);
}
