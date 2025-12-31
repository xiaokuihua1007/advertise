package org.classmatechen.sample.config.baidu;

import java.util.List;

import javax.annotation.PostConstruct;

import org.classmatechen.baidu.BaiduInit;
import org.classmatechen.baidu.client.impl.AccessTokenProvider;
import org.classmatechen.baidu.client.impl.init.BaiduProvider;
import org.classmatechen.baidu.client.impl.refresh.RefreshTokenProvider;

// @Component
public class BdInit {

    private List<AccessTokenProvider> providers0;
    private List<RefreshTokenProvider> providers1;
    private List<BaiduProvider> providers2;

    public BdInit(
        List<AccessTokenProvider> providers0,
        List<RefreshTokenProvider> providers1,
        List<BaiduProvider> providers2
    ) {
        this.providers0 = providers0;
        this.providers1 = providers1;
        this.providers2 = providers2;
    }

    @PostConstruct
    private void init() {

        new BaiduInit(providers0, providers1, providers2);
    }
}
