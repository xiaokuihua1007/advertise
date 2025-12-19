package org.classmatechen.core.domain.platform.account.error;

import org.classmatechen.core.domain.platform.account.vo.AccountId;

public class AccountNoExistException extends RuntimeException {

    public AccountNoExistException(AccountId id) {
        super("no exist account, id:" + id.accountId() + ", platform" + id.platform().getName());
    }
}
