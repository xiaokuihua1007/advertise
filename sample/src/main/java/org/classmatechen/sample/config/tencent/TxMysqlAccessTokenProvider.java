package org.classmatechen.sample.config.tencent;

import java.util.Objects;

import org.classmatechen.basic.Context;
import org.classmatechen.basic.pubsub.AccessTokenExpiredListener;
import org.classmatechen.basic.pubsub.Publisher;
import org.classmatechen.sample.mapper.TencentMapper;
import org.classmatechen.sample.po.Tencent;
import org.classmatechen.tencent.TxContext;
import org.classmatechen.tencent.client.impl.AccessTokenProvider;
import org.springframework.stereotype.Component;

@Component
public class TxMysqlAccessTokenProvider implements
                                                AccessTokenProvider,
                                                AccessTokenExpiredListener {

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
    public void onAccessTokenExpired(Context context) {
        if (context instanceof TxContext) {
            this.mapper.clearAccessToken(((TxContext) context).getClientId());
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
