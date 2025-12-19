package org.classmatechen.core.controller;

import java.util.List;

import org.classmatechen.core.application.paltform.account.AccountAppService;
import org.classmatechen.core.application.paltform.account.command.PageAccountCommand;
import org.classmatechen.core.application.paltform.account.dto.AccountDto;
import org.classmatechen.core.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/account")
public class AccountController {

    @Autowired
    private AccountAppService appService;

    @GetMapping("/page")
    public Result<List<AccountDto>> page() {

        PageAccountCommand command = new PageAccountCommand();
        List<AccountDto> accounts = appService.page(command);
        return Result.ok(accounts);
    }
}
