package org.classmatechen.sample.config.tencent;

import java.util.List;

import javax.annotation.PostConstruct;

import org.classmatechen.tencent.TencentInit;
import org.classmatechen.tencent.client.impl.AccessTokenProvider;
import org.classmatechen.tencent.client.impl.init.TencentProvider;
import org.classmatechen.tencent.client.impl.refresh.RefreshTokenProvider;

// @Component
public class TxInit {

    private List<AccessTokenProvider> providers0;
    private List<RefreshTokenProvider> providers1;
    private List<TencentProvider> providers2;

    public TxInit(
        List<AccessTokenProvider> providers0,
        List<RefreshTokenProvider> providers1,
        List<TencentProvider> providers2
    ) {
        this.providers0 = providers0;
        this.providers1 = providers1;
        this.providers2 = providers2;
    }

    @PostConstruct
    private void init() {

        new TencentInit(providers0, providers1, providers2);
    }
}
