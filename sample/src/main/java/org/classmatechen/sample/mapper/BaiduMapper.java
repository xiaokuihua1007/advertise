package org.classmatechen.sample.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.classmatechen.sample.po.Baidu;

@Mapper
public interface BaiduMapper {

    Baidu select(String userId);

    List<Baidu> list();
}
