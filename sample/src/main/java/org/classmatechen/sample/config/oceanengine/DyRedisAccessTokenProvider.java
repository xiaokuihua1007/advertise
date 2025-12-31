package org.classmatechen.sample.config.oceanengine;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.classmatechen.basic.Context;
import org.classmatechen.basic.pubsub.AccessTokenExpiredListener;
import org.classmatechen.basic.pubsub.AccessTokenRefreshedEvent;
import org.classmatechen.basic.pubsub.AccessTokenRefreshedListener;
import org.classmatechen.basic.pubsub.Publisher;
import org.classmatechen.basic.pubsub.RefreshTokenRefreshedListener;
import org.classmatechen.oceanengine.DyContext;
import org.classmatechen.oceanengine.client.impl.AccessTokenProvider;
import org.classmatechen.oceanengine.client.impl.refresh.RefreshDep;
import org.classmatechen.oceanengine.client.impl.refresh.RefreshTokenProvider;
import org.classmatechen.sample.mapper.OceanengineMapper;
import org.classmatechen.sample.po.Oceanengine;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class DyRedisAccessTokenProvider implements
                                                AccessTokenProvider,
                                                AccessTokenRefreshedListener,
                                                AccessTokenExpiredListener,
                                                RefreshTokenProvider,
                                                RefreshTokenRefreshedListener {

    private static final String ACCESS_TOKEN = "ad:accessToken:ccAdvertiserId:";
    private static final String REFRESH_TOKEN = "ad:refreshToken:ccAdvertiserId:";
    private static final Map<Long, Long> map;
    private final OceanengineMapper mapper;
    private final StringRedisTemplate redisTemplate;

    static {
        map = new HashMap<>();
        map.put(1833179468410587L, 1692853395087360L);
    }

    public DyRedisAccessTokenProvider(OceanengineMapper mapper, StringRedisTemplate redisTemplate) {
        this.mapper = mapper;
        this.redisTemplate = redisTemplate;
        Publisher.subscribe(this);
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public String accessToken(DyContext context) {

        Oceanengine oceanengine = this.mapper.select(context.getAppId());
        if (Objects.isNull(oceanengine)) {
            return null;
        }
        Long id = map.get(oceanengine.getAppId());
        if (null != id) {
            String accessToken = this.redisTemplate.opsForValue().get(ACCESS_TOKEN + id);
            if (null != accessToken) {
                Publisher.publish(new AccessTokenRefreshedEvent(context, accessToken));
                return accessToken;
            }
        }
        return null;
    }

    @Override
    public void onAccessTokenRefreshed(Context context, String token) {

        if (!(context instanceof DyContext)) {
            return;
        }
        DyContext dyContext = (DyContext) context;
        Oceanengine oceanengine = this.mapper.select(dyContext.getAppId());
        if (Objects.isNull(oceanengine)) {
            return;
        }
        Long id = map.get(oceanengine.getAppId());
        if (null != id) {
            this.redisTemplate.opsForValue().set(ACCESS_TOKEN + id, token);
        }
    }

    @Override
    public void onAccessTokenExpired(Context context) {

        if (!(context instanceof DyContext)) {
            return;
        }
        DyContext dyContext = (DyContext) context;
        Oceanengine oceanengine = this.mapper.select(dyContext.getAppId());
        if (Objects.isNull(oceanengine)) {
            return;
        }
        Long id = map.get(oceanengine.getAppId());
        if (null != id) {
            this.redisTemplate.delete(ACCESS_TOKEN + id);
        }
    }

    @Override
    public RefreshDep refreshDep(DyContext context) {

        Oceanengine oceanengine = this.mapper.select(context.getAppId());
        if (Objects.isNull(oceanengine)) {
            return null;
        }
        String refreshToken = null;
        Long id = map.get(oceanengine.getAppId());
        if (null != id) {
            refreshToken = this.redisTemplate.opsForValue().get(REFRESH_TOKEN + id);
        }
        if (null == refreshToken) {
            return null;
        }
        RefreshDep dep = new RefreshDep();
        dep.setAppId(oceanengine.getAppId());
        dep.setRefreshToken(refreshToken);
        dep.setSecret(oceanengine.getSecret());
        return dep;
    }

    @Override
    public void onRefreshTokenRefreshed(Context context, String token) {

        if (!(context instanceof DyContext)) {
            return;
        }
        DyContext dyContext = (DyContext) context;
        Oceanengine oceanengine = this.mapper.select(dyContext.getAppId());
        if (Objects.isNull(oceanengine)) {
            return;
        }
        Long id = map.get(oceanengine.getAppId());
        if (null != id) {
            this.redisTemplate.opsForValue().set(REFRESH_TOKEN + id, token);
        }
    }
}
