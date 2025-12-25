package org.classmatechen.sample.config.oceanengine;

import java.util.Objects;

import org.classmatechen.basic.Context;
import org.classmatechen.basic.pubsub.AccessTokenExpiredListener;
import org.classmatechen.basic.pubsub.Publisher;
import org.classmatechen.oceanengine.DyContext;
import org.classmatechen.oceanengine.client.impl.AccessTokenProvider;
import org.classmatechen.sample.mapper.OceanengineMapper;
import org.classmatechen.sample.po.Oceanengine;
import org.springframework.stereotype.Component;

@Component
public class DyMysqlAccessTokenProvider implements
                                                AccessTokenProvider,
                                                AccessTokenExpiredListener {

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
    public void onAccessTokenExpired(Context context) {
        if (context instanceof DyContext) {
            this.mapper.clearAccessToken(((DyContext) context).getAppId());
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
