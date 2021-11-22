package com.devexperts.repository;

import com.devexperts.model.account.Account;
import com.devexperts.model.account.AccountKey;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccountRepository {
    private final Map<AccountKey, Account> accounts = new ConcurrentHashMap<>();

    public Account getAccount(AccountKey accountKey){
        return accounts.get(accountKey);
    }

    public void createAccount(Account account){
        accounts.put(account.getAccountKey(), account);
    }

    public void clear(){
        accounts.clear();
    }

    public void updateAccount(Account account) {
        accounts.put(account.getAccountKey(), account);
    }
}
