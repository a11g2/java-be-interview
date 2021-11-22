package com.devexperts.exception;

import com.devexperts.model.account.AccountKey;

public class BalanceNotEnoughException extends RuntimeException {
    public BalanceNotEnoughException(AccountKey accountKey, double balance) {
        super(String.format("Balance not enough in account %s, with balance %s", accountKey, balance));
    }
}