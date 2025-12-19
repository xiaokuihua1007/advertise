package org.classmatechen.sample.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OceanengineMapper {

    @Update("update oceanengine set access_token = null where app_id = #{ appId }")
    void clearAccessToken(Long appId);

    @Update("update oceanengine set refresh_token = null where app_id = #{ appId }")
    void clearRefreshToken(Long appId);

    @Update("update oceanengine set access_token = #{ accessToken } where app_id = #{ appId }")
    void saveAccessToken(@Param("appId") Long appId, @Param("accessToken") String accessToken);

    @Select("select app_id, secret, auth_code, refresh_token, access_token from oceanengine where app_id = #{ appId }")
    Oceanengine select(Long appId);

    @Select("select app_id, secret, auth_code, refresh_token, access_token from oceanengine")
    List<Oceanengine> list();
}
