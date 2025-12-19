package org.classmatechen.core.domain.platform.account.vo;

import java.util.Objects;

import org.classmatechen.common.Platform;

public class AccountId {

    private final Long accountId;
    private final Platform platform;

    public AccountId(Long accountId, Platform platform) {

        if (null == accountId || accountId <= 0 || null == platform) {
            throw new IllegalArgumentException();
        }
        this.accountId = accountId;
        this.platform = platform;
    }

    @Override
    public boolean equals(Object object) {

        if (null == object || !(object instanceof AccountId)) {
            return false;
        }
        return Objects.equals(this.accountId, ((AccountId) object).accountId) && Objects.equals(this.platform, ((AccountId) object).platform);
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.accountId, this.platform.getId());
    }

    public Platform platform() {

        return this.platform;
    }

    public Long accountId() {

        return this.accountId;
    }
}
