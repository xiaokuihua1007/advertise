package org.classmatechen.core.infrastructure.third.impl;

import java.util.Objects;

import org.classmatechen.common.Platform;
import org.classmatechen.core.infrastructure.third.impl.client.Client;
import org.classmatechen.core.infrastructure.third.impl.client.DyClient;

public class ClientFactory {

    public Client getClient(Platform platform) {

        Client client;
        switch (platform) {
            case Oceanengine:
                client = new DyClient();
                break;
            default:
                client = null;
                break;
        }
        if (Objects.isNull(client)) {
            throw new RuntimeException();
        }
        return client;
    }
}
