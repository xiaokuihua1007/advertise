package org.classmatechen.sample.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.classmatechen.sample.po.Baidu;

@Mapper
public interface BaiduMapper {

    @Update("update baidu set access_token = null where user_id = #{ userId }")
    void clearAccessToken(String userId);

    @Update("update baidu set refresh_token = null where user_id = #{ userId }")
    void clearRefreshToken(String userId);

    @Update("update baidu set access_token = #{ accessToken } where user_id = #{ userId }")
    void saveAccessToken(@Param("userId") String userId, @Param("accessToken") String accessToken);

    @Select("select user_id, app_id, auth_code, secret_key, access_token, refresh_token from baidu where user_id = #{ userId }")
    Baidu select(String userId);

    @Select("select user_id, app_id, auth_code, secret_key, access_token, refresh_token from baidu")
    List<Baidu> list();
}
