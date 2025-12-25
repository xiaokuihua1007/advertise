package org.classmatechen.sample.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.classmatechen.sample.po.AdvertiseMetrics;

import com.baomidou.dynamic.datasource.annotation.DS;

@Mapper
@DS("doris")
public interface AdvertiseMetricsMapper {

    List<AdvertiseMetrics> list();

    void insert(List<AdvertiseMetrics> metrics);
}
