package org.classmatechen.sample.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.classmatechen.sample.po.PlatformVideo;
import org.classmatechen.sample.query.PlatformVideoQuery;

@Mapper
public interface PlatformVideoMapper {

    List<PlatformVideo> list(PlatformVideoQuery query);

    void updatePath(PlatformVideo video);
}
