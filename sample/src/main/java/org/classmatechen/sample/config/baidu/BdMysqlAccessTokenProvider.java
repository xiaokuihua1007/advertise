package org.classmatechen.sample.config.baidu;

import java.util.Objects;

import org.classmatechen.baidu.BdContext;
import org.classmatechen.baidu.client.impl.AccessTokenProvider;
import org.classmatechen.basic.Context;
import org.classmatechen.basic.pubsub.AccessTokenExpiredListener;
import org.classmatechen.basic.pubsub.Publisher;
import org.classmatechen.sample.mapper.BaiduMapper;
import org.classmatechen.sample.po.Baidu;
import org.springframework.stereotype.Component;

@Component
public class BdMysqlAccessTokenProvider implements
                                                AccessTokenProvider,
                                                AccessTokenExpiredListener {

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
    public void onAccessTokenExpired(Context context) {
        if (context instanceof BdContext) {
            this.mapper.clearAccessToken(((BdContext) context).getAppId());
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
