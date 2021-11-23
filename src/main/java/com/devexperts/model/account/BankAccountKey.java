package com.devexperts.model.account;

import java.util.Objects;

public class BankAccountKey extends AccountKey implements Comparable<BankAccountKey>{

    protected BankAccountKey(long accountId) {
        super(accountId);
    }

    public static BankAccountKey valueOf(long accountId) {
        return new BankAccountKey(accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccountKey other = (BankAccountKey) o;
        return Objects.equals(accountId, other.accountId);
    }

    @Override
    public int compareTo(BankAccountKey o) {
        return Long.compare(accountId, o.accountId);
    }
}
