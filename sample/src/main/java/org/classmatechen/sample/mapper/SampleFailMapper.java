package org.classmatechen.sample.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.classmatechen.sample.po.SampleFail;
import org.classmatechen.sample.query.SampleFailQuery;

@Mapper
public interface SampleFailMapper {

    void insert(SampleFail fail);

    void batchInsert(List<SampleFail> list);

    List<SampleFail> list(SampleFailQuery query);

    void deleteById(List<Long> id);

    List<SampleFail> groupBySampler();
}
