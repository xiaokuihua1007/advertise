package org.classmatechen.sample.config.oceanengine;

import java.util.List;

import javax.annotation.PostConstruct;

import org.classmatechen.oceanengine.OceanengineInit;
import org.classmatechen.oceanengine.client.impl.AccessTokenProvider;
import org.classmatechen.oceanengine.client.impl.init.OceanengineProvider;
import org.classmatechen.oceanengine.client.impl.refresh.RefreshTokenProvider;
import org.springframework.stereotype.Component;

@Component
public class DyInit {

    private List<AccessTokenProvider> providers0;
    private List<RefreshTokenProvider> providers1;
    private List<OceanengineProvider> providers2;

    public DyInit(
        List<AccessTokenProvider> providers0,
        List<RefreshTokenProvider> providers1,
        List<OceanengineProvider> providers2
    ) {
        this.providers0 = providers0;
        this.providers1 = providers1;
        this.providers2 = providers2;
    }

    @PostConstruct
    private void init() {

        new OceanengineInit(providers0, providers1, providers2);
    }
}
