package org.classmatechen.sample.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.classmatechen.sample.po.Tencent;

@Mapper
public interface TencentMapper {

    Tencent select(Long clientId);

    List<Tencent> list();
}
