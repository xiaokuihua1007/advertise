package org.classmatechen.core.domain.platform.advertise.vo;

import java.util.Objects;

import org.classmatechen.common.Platform;

public class AdvertiseId {

    private final Long advertiseId;
    private final Platform platform;

    public AdvertiseId(Long advertiseId, Platform platform) {

        if (null == advertiseId || advertiseId <= 0 || null == platform) {
            throw new IllegalArgumentException();
        }
        this.advertiseId = advertiseId;
        this.platform = platform;
    }

    @Override
    public boolean equals(Object object) {

        if (null == object || !(object instanceof AdvertiseId)) {
            return false;
        }
        return Objects.equals(this.advertiseId, ((AdvertiseId) object).advertiseId) && Objects.equals(this.platform, ((AdvertiseId) object).platform);
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.advertiseId, this.platform.getId());
    }

    public Platform platform() {

        return this.platform;
    }
}
