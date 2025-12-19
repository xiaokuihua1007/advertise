package org.classmatechen.sample.config.tencent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.classmatechen.basic.Context;
import org.classmatechen.basic.pubsub.AccessTokenExpiredListener;
import org.classmatechen.basic.pubsub.AccessTokenRefreshedListener;
import org.classmatechen.basic.pubsub.Publisher;
import org.classmatechen.tencent.TxContext;
import org.classmatechen.tencent.client.impl.AccessTokenProvider;
import org.springframework.stereotype.Component;

@Component
public class TxLocalAccessTokenProvider implements
                                                AccessTokenProvider,
                                                AccessTokenExpiredListener,
                                                AccessTokenRefreshedListener {

    private static final Map<Long, String> cache;

    static {
        cache = new ConcurrentHashMap<>();
    }

    public TxLocalAccessTokenProvider() {
        Publisher.subscribe(this);
    }

    @Override
    public String accessToken(TxContext context) {
        return cache.get(context.getClientId());
    }

    @Override
    public void onAccessTokenRefreshed(Context context, String accessToken) {
        if (context instanceof TxContext) {
            cache.put(((TxContext) context).getClientId(), accessToken);
        }
    }

    @Override
    public void onAccessTokenExpired(Context context) {
        if (context instanceof TxContext) {
            cache.remove(((TxContext) context).getClientId());
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
