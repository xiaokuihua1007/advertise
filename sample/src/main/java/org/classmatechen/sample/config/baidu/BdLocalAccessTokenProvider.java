package org.classmatechen.sample.config.baidu;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.classmatechen.baidu.BdContext;
import org.classmatechen.basic.Context;
import org.classmatechen.basic.pubsub.AccessTokenExpiredListener;
import org.classmatechen.basic.pubsub.AccessTokenRefreshedListener;
import org.classmatechen.basic.pubsub.Publisher;
import org.classmatechen.baidu.client.impl.AccessTokenProvider;
import org.springframework.stereotype.Component;

@Component
public class BdLocalAccessTokenProvider implements
                                                AccessTokenProvider,
                                                AccessTokenExpiredListener,
                                                AccessTokenRefreshedListener {

    private static final Map<String, String> cache;

    static {
        cache = new ConcurrentHashMap<>();
    }

    public BdLocalAccessTokenProvider() {
        Publisher.subscribe(this);
    }

    @Override
    public String accessToken(BdContext context) {
        return cache.get(context.getAppId());
    }

    @Override
    public void onAccessTokenRefreshed(Context context, String accessToken) {
        if (context instanceof BdContext) {
            cache.put(((BdContext) context).getAppId(), accessToken);
        }
    }

    @Override
    public void onAccessTokenExpired(Context context) {
        if (context instanceof BdContext) {
            cache.remove(((BdContext) context).getAppId());
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
