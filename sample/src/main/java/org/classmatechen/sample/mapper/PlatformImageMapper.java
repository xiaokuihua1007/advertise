package org.classmatechen.sample.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.classmatechen.sample.po.PlatformImage;
import org.classmatechen.sample.query.PlatformImageQuery;

@Mapper
public interface PlatformImageMapper {

    List<PlatformImage> list(PlatformImageQuery query);

    void updatePath(PlatformImage image);
}
