package org.classmatechen.core.domain.platform.account.vo;

import org.classmatechen.common.Platform;

public record AccountId(Long accountId, Platform platform) {

    public AccountId {

        if (null == accountId || accountId <= 0 || null == platform) {
            throw new IllegalArgumentException();
        }
    }
}
