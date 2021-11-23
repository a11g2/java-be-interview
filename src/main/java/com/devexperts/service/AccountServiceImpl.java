package com.devexperts.service;

import com.devexperts.exception.AccountNotFoundException;
import com.devexperts.exception.BalanceNotEnoughException;
import com.devexperts.exception.TransferFailedException;
import com.devexperts.model.account.Account;
import com.devexperts.model.account.AccountKey;
import com.devexperts.model.account.BankAccount;
import com.devexperts.model.service.AccountService;
import com.devexperts.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.Objects;


@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);


    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void clear() {
        accountRepository.clear();
    }

    @Override
    public void createAccount(Account account) {
        accountRepository.createAccount(account);
    }

    @Override
    public Account getAccount(long id) {
        Account account = accountRepository.getAccount(AccountKey.valueOf(id));
        if (Objects.isNull(account))
            throw new AccountNotFoundException("Account not found by id " + id);

        return account;
    }

    @Override
    public void transfer(Account source, Account target, double amount) {

        if (source.getBalance().compareTo(amount) < 0) {
            throw new BalanceNotEnoughException(source.getAccountKey(), source.getBalance());
        }

        if (!(source instanceof BankAccount)) {
            logger.error("source is NOT instance of BankAccount");
            return;
        }
        BankAccount sourceAccount = (BankAccount) source;

        if (!(target instanceof BankAccount)) {
            logger.error("target is NOT instance of BankAccount");
            return;
        }
        BankAccount targetAccount = (BankAccount) target;

        Account lock1;
        Account lock2;
        if (sourceAccount.compareTo(targetAccount) > 0) {
            lock1 = source;
            lock2 = target;

        } else {
            lock1 = target;
            lock2 = source;
        }

        synchronized (lock1) {
            updateAccountBalance(sourceAccount, amount, false);
            synchronized (lock2) {
                try {
                    updateAccountBalance(targetAccount, amount, true);
                } catch (TransferFailedException transferFailedException) {
                    logger.error("failed transfer amount between accounts");
                    updateAccountBalance(sourceAccount, amount, true);
                    throw new TransferFailedException();
                }
            }
        }
    }

    private void updateAccountBalance(BankAccount account, double amount, boolean increment) {
        account.setBalance(increment ? (account.getBalance() + amount) : (account.getBalance() - amount));
        accountRepository.updateAccount(account);
    }
}