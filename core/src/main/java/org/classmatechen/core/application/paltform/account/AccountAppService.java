package org.classmatechen.core.application.paltform.account;

import java.util.List;
import java.util.stream.Collectors;

import org.classmatechen.core.application.paltform.account.command.PageAccountCommand;
import org.classmatechen.core.application.paltform.account.dto.AccountDto;
import org.classmatechen.core.domain.platform.account.entity.Account;
import org.classmatechen.core.domain.platform.account.query.AccountQuery;
import org.classmatechen.core.domain.platform.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountAppService {

    @Autowired
    private AccountRepository accountRepository;

    public List<AccountDto> page(PageAccountCommand command) {

        AccountQuery query = new AccountQuery();
        List<Account> accounts = this.accountRepository.list(query);
        return accounts.stream().map(this::converte).collect(Collectors.toList());
    }

    private AccountDto converte(Account account) {
        return null;
    }
}
