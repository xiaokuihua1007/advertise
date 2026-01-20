package org.classmatechen.core.domain.platform.account.vo;

import org.classmatechen.core.common.CEnum;

public class AccountStatus extends CEnum {

    /**
     * 正常
     */
    public static final AccountStatus RUNNING = new AccountStatus(0, "RUNNING");

    /**
     * 禁用
     */
    public static final AccountStatus DISABLE = new AccountStatus(2, "DISABLE");

    private AccountStatus(int code, String lable) {
        super(code, lable);
    }
}
