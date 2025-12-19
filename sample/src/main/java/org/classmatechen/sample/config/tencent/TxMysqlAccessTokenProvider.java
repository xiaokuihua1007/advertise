package org.classmatechen.sample.config.tencent;

import java.util.Objects;

import org.classmatechen.basic.Context;
import org.classmatechen.basic.pubsub.AccessTokenExpiredListener;
import org.classmatechen.basic.pubsub.AccessTokenRefreshedListener;
import org.classmatechen.basic.pubsub.Publisher;
import org.classmatechen.basic.pubsub.RefreshTokenExpiredListener;
import org.classmatechen.sample.mapper.Tencent;
import org.classmatechen.sample.mapper.TencentMapper;
import org.classmatechen.tencent.TxContext;
import org.classmatechen.tencent.client.impl.AccessTokenProvider;
import org.classmatechen.tencent.client.impl.refresh.RefreshDep;
import org.classmatechen.tencent.client.impl.refresh.RefreshTokenProvider;
import org.springframework.stereotype.Component;

@Component
public class TxMysqlAccessTokenProvider implements
                                                AccessTokenProvider,
                                                RefreshTokenProvider,
                                                AccessTokenExpiredListener,
                                                AccessTokenRefreshedListener,
                                                RefreshTokenExpiredListener {

    private final TencentMapper mapper;

    public TxMysqlAccessTokenProvider(TencentMapper mapper) {
        this.mapper = mapper;
        Publisher.subscribe(this);
    }

    @Override
    public String accessToken(TxContext context) {
        Tencent tencent = this.mapper.select(context.getClientId());
        if (Objects.nonNull(tencent)) {
            return tencent.getAccessToken();
        }
        return null;
    }

    @Override
    public RefreshDep refreshDep(TxContext context) {
        Tencent tencent = this.mapper.select(context.getClientId());
        if (Objects.nonNull(tencent)) {
            RefreshDep dep = new RefreshDep();
            dep.setClientId(tencent.getClientId());
            dep.setClientSecret(tencent.getClientSecret());
            dep.setRedirectUri(tencent.getRedirectUri());
            dep.setRefreshToken(tencent.getRefreshToken());
            return dep;
        }
        return null;
    }

    @Override
    public void onAccessTokenExpired(Context context) {
        if (context instanceof TxContext) {
            this.mapper.clearAccessToken(((TxContext) context).getClientId());
        }
    }

    @Override
    public void onAccessTokenRefreshed(Context context, String accessToken) {
        if (context instanceof TxContext) {
            this.mapper.saveAccessToken(((TxContext) context).getClientId(), accessToken);
        }
    }

    @Override
    public void onRefreshTokenExpired(Context context) {
        if (context instanceof TxContext) {
            this.mapper.clearRefreshToken(((TxContext) context).getClientId());
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
