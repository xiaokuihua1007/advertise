package org.classmatechen.sample.config.baidu;

import java.util.Objects;

import org.classmatechen.baidu.BdContext;
import org.classmatechen.baidu.client.impl.AccessTokenProvider;
import org.classmatechen.baidu.client.impl.refresh.RefreshDep;
import org.classmatechen.baidu.client.impl.refresh.RefreshTokenProvider;
import org.classmatechen.basic.Context;
import org.classmatechen.basic.pubsub.AccessTokenExpiredListener;
import org.classmatechen.basic.pubsub.AccessTokenRefreshedListener;
import org.classmatechen.basic.pubsub.Publisher;
import org.classmatechen.basic.pubsub.RefreshTokenExpiredListener;
import org.classmatechen.sample.mapper.Baidu;
import org.classmatechen.sample.mapper.BaiduMapper;
import org.springframework.stereotype.Component;

@Component
public class BdMysqlAccessTokenProvider implements
                                                AccessTokenProvider,
                                                RefreshTokenProvider,
                                                AccessTokenExpiredListener,
                                                AccessTokenRefreshedListener,
                                                RefreshTokenExpiredListener {

    private final BaiduMapper mapper;

    public BdMysqlAccessTokenProvider(BaiduMapper mapper) {
        this.mapper = mapper;
        Publisher.subscribe(this);
    }

    @Override
    public String accessToken(BdContext context) {
        Baidu baidu = this.mapper.select(context.getAppId());
        if (Objects.nonNull(baidu)) {
            return baidu.getAccessToken();
        }
        return null;
    }

    @Override
    public RefreshDep refreshDep(BdContext context) {
        Baidu baidu = this.mapper.select(context.getAppId());
        if (Objects.nonNull(baidu)) {
            RefreshDep dep = new RefreshDep();
            dep.setAppId(baidu.getAppId());
            dep.setRefreshToken(baidu.getRefreshToken());
            dep.setSecretKey(baidu.getSecretKey());
            dep.setUserId(baidu.getUserId());
            return dep;
        }
        return null;
    }

    @Override
    public void onAccessTokenExpired(Context context) {
        if (context instanceof BdContext) {
            this.mapper.clearAccessToken(((BdContext) context).getAppId());
        }
    }

    @Override
    public void onAccessTokenRefreshed(Context context, String accessToken) {
        if (context instanceof BdContext) {
            this.mapper.saveAccessToken(((BdContext) context).getAppId(), accessToken);
        }
    }

    @Override
    public void onRefreshTokenExpired(Context context) {
        if (context instanceof BdContext) {
            this.mapper.clearRefreshToken(((BdContext) context).getAppId());
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
