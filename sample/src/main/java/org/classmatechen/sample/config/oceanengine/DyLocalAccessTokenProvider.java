package org.classmatechen.sample.config.oceanengine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.classmatechen.basic.Context;
import org.classmatechen.basic.pubsub.AccessTokenExpiredListener;
import org.classmatechen.basic.pubsub.AccessTokenRefreshedListener;
import org.classmatechen.basic.pubsub.Publisher;
import org.classmatechen.oceanengine.DyContext;
import org.classmatechen.oceanengine.client.impl.AccessTokenProvider;
import org.springframework.stereotype.Component;

@Component
public class DyLocalAccessTokenProvider implements
                                                AccessTokenProvider,
                                                AccessTokenExpiredListener,
                                                AccessTokenRefreshedListener {

    private static final Map<Long, String> cache;

    static {
        cache = new ConcurrentHashMap<>();
    }

    public DyLocalAccessTokenProvider() {
        Publisher.subscribe(this);
    }

    @Override
    public String accessToken(DyContext context) {
        return cache.get(context.getAppId());
    }

    @Override
    public void onAccessTokenRefreshed(Context context, String accessToken) {
        if (context instanceof DyContext) {
            cache.put(((DyContext) context).getAppId(), accessToken);
        }
    }

    @Override
    public void onAccessTokenExpired(Context context) {
        if (context instanceof DyContext) {
            cache.remove(((DyContext) context).getAppId());
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
