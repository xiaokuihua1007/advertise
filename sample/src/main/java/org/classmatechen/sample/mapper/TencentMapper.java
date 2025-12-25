package org.classmatechen.sample.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.classmatechen.sample.po.Tencent;

@Mapper
public interface TencentMapper {

    @Update("update tencent set access_token = null where client_id = #{ clientId }")
    void clearAccessToken(Long clientId);

    @Update("update tencent set refresh_token = null where client_id = #{ clientId }")
    void clearRefreshToken(Long clientId);

    @Update("update tencent set access_token = #{ accessToken } where client_id = #{ clientId }")
    void saveAccessToken(@Param("clientId") Long clientId, @Param("accessToken") String accessToken);

    @Select("select client_id, client_secret, authorization_code, redirect_uri, access_token, refresh_token from tencent where client_id = #{ clientId }")
    Tencent select(Long clientId);

    @Select("select client_id, client_secret, authorization_code, redirect_uri, access_token, refresh_token from tencent")
    List<Tencent> list();
}
