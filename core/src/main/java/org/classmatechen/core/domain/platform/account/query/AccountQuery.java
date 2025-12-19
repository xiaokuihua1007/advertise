package org.classmatechen.core.domain.platform.account.query;

import org.classmatechen.common.Platform;
import lombok.Data;

@Data
public class AccountQuery {

    private Long accountId;
    private Platform platform;
}
