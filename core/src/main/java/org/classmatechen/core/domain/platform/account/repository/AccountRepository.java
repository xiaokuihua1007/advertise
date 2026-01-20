package org.classmatechen.core.domain.platform.account.repository;

import java.util.List;

import org.classmatechen.core.domain.platform.account.entity.Account;
import org.classmatechen.core.domain.platform.account.query.AccountQuery;
import org.classmatechen.core.domain.platform.account.vo.AccountId;

public interface AccountRepository {

    Account findById(AccountId id);

    List<Account> list(AccountQuery query);
}
