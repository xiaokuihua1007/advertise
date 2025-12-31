package org.classmatechen.sample.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.classmatechen.sample.po.Oceanengine;

@Mapper
public interface OceanengineMapper {

    Oceanengine select(Long appId);

    List<Oceanengine> list();
}
