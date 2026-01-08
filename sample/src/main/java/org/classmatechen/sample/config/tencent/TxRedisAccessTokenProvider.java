package org.classmatechen.sample.config.tencent;

import java.util.Objects;

import org.classmatechen.basic.Context;
import org.classmatechen.basic.pubsub.AccessTokenExpiredListener;
import org.classmatechen.basic.pubsub.AccessTokenRefreshedEvent;
import org.classmatechen.basic.pubsub.AccessTokenRefreshedListener;
import org.classmatechen.basic.pubsub.Publisher;
import org.classmatechen.basic.pubsub.RefreshTokenRefreshedListener;
import org.classmatechen.tencent.TxContext;
import org.classmatechen.tencent.client.impl.AccessTokenProvider;
import org.classmatechen.tencent.client.impl.refresh.RefreshDep;
import org.classmatechen.tencent.client.impl.refresh.RefreshTokenProvider;
import org.classmatechen.sample.mapper.TencentMapper;
import org.classmatechen.sample.po.Tencent;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class TxRedisAccessTokenProvider implements
                                                AccessTokenProvider,
                                                AccessTokenRefreshedListener,
                                                AccessTokenExpiredListener,
                                                RefreshTokenProvider,
                                                RefreshTokenRefreshedListener {

    private static final String ACCESS_TOKEN = "ad:accessToken:ccAdvertiserId:";
    private static final String REFRESH_TOKEN = "ad:refreshToken:ccAdvertiserId:";
    private final TencentMapper mapper;
    private final StringRedisTemplate redisTemplate;

    public TxRedisAccessTokenProvider(TencentMapper mapper, StringRedisTemplate redisTemplate) {
        this.mapper = mapper;
        this.redisTemplate = redisTemplate;
        Publisher.subscribe(this);
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public String accessToken(TxContext context) {

        Tencent tencent = this.mapper.select(context.getClientId());
        if (Objects.isNull(tencent)) {
            return null;
        }
        String accessToken = this.redisTemplate.opsForValue().get(ACCESS_TOKEN + tencent.getClientId());
        if (null != accessToken) {
            Publisher.publish(new AccessTokenRefreshedEvent(context, accessToken));
            return accessToken;
        }
        return null;
    }

    @Override
    public void onAccessTokenRefreshed(Context context, String token) {

        if (!(context instanceof TxContext)) {
            return;
        }
        TxContext txContext = (TxContext) context;
        Tencent tencent = this.mapper.select(txContext.getClientId());
        if (Objects.isNull(tencent)) {
            return;
        }
        this.redisTemplate.opsForValue().set(ACCESS_TOKEN + tencent.getClientId(), token);
    }

    @Override
    public void onAccessTokenExpired(Context context) {

        if (!(context instanceof TxContext)) {
            return;
        }
        TxContext txContext = (TxContext) context;
        Tencent tencent = this.mapper.select(txContext.getClientId());
        if (Objects.isNull(tencent)) {
            return;
        }
        this.redisTemplate.delete(ACCESS_TOKEN + tencent.getClientId());
    }

    @Override
    public RefreshDep refreshDep(TxContext context) {

        Tencent tencent = this.mapper.select(context.getClientId());
        if (Objects.isNull(tencent)) {
            return null;
        }
        String refreshToken = this.redisTemplate.opsForValue().get(REFRESH_TOKEN + tencent.getClientId());
        RefreshDep dep = new RefreshDep();
        dep.setClientId(tencent.getClientId());
        dep.setClientSecret(tencent.getClientSecret());
        dep.setRefreshToken(refreshToken);
        dep.setRedirectUri(tencent.getRedirectUri());
        return dep;
    }

    @Override
    public void onRefreshTokenRefreshed(Context context, String token) {

        if (!(context instanceof TxContext)) {
            return;
        }
        TxContext txContext = (TxContext) context;
        Tencent tencent = this.mapper.select(txContext.getClientId());
        if (Objects.isNull(tencent)) {
            return;
        }
        this.redisTemplate.opsForValue().set(REFRESH_TOKEN + tencent.getClientId(), token);
    }
}
