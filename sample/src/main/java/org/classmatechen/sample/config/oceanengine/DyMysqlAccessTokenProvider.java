package org.classmatechen.sample.config.oceanengine;

import java.util.Objects;

import org.classmatechen.basic.Context;
import org.classmatechen.basic.pubsub.AccessTokenExpiredListener;
import org.classmatechen.basic.pubsub.AccessTokenRefreshedListener;
import org.classmatechen.basic.pubsub.Publisher;
import org.classmatechen.basic.pubsub.RefreshTokenExpiredListener;
import org.classmatechen.oceanengine.DyContext;
import org.classmatechen.oceanengine.client.impl.AccessTokenProvider;
import org.classmatechen.oceanengine.client.impl.refresh.RefreshDep;
import org.classmatechen.oceanengine.client.impl.refresh.RefreshTokenProvider;
import org.classmatechen.sample.mapper.Oceanengine;
import org.classmatechen.sample.mapper.OceanengineMapper;
import org.springframework.stereotype.Component;

@Component
public class DyMysqlAccessTokenProvider implements
                                                AccessTokenProvider,
                                                RefreshTokenProvider,
                                                AccessTokenExpiredListener,
                                                AccessTokenRefreshedListener,
                                                RefreshTokenExpiredListener {

    private final OceanengineMapper mapper;

    public DyMysqlAccessTokenProvider(OceanengineMapper mapper) {
        this.mapper = mapper;
        Publisher.subscribe(this);
    }

    @Override
    public String accessToken(DyContext context) {
        Oceanengine oceanengine = this.mapper.select(context.getAppId());
        if (Objects.nonNull(oceanengine)) {
            return oceanengine.getAccessToken();
        }
        return null;
    }

    @Override
    public RefreshDep refreshDep(DyContext context) {
        Oceanengine oceanengine = this.mapper.select(context.getAppId());
        if (Objects.nonNull(oceanengine)) {
            RefreshDep dep = new RefreshDep();
            dep.setAppId(oceanengine.getAppId());
            dep.setSecret(oceanengine.getSecret());
            dep.setRefreshToken(oceanengine.getRefreshToken());
            return dep;
        }
        return null;
    }

    @Override
    public void onAccessTokenExpired(Context context) {
        if (context instanceof DyContext) {
            this.mapper.clearAccessToken(((DyContext) context).getAppId());
        }
    }

    @Override
    public void onAccessTokenRefreshed(Context context, String accessToken) {
        if (context instanceof DyContext) {
            this.mapper.saveAccessToken(((DyContext) context).getAppId(), accessToken);
        }
    }

    @Override
    public void onRefreshTokenExpired(Context context) {
        if (context instanceof DyContext) {
            this.mapper.clearRefreshToken(((DyContext) context).getAppId());
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
