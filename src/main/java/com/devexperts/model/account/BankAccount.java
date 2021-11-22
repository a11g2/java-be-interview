package com.devexperts.model.account;


import java.util.Comparator;
import java.util.Objects;

public class BankAccount extends Account implements Comparable<BankAccount> {

    public BankAccount(BankAccountKey accountKey, String firstName, String lastName, Double balance) {
        super(accountKey, firstName, lastName, balance);
    }

    public BankAccountKey getBankAccountKey() {
        return (BankAccountKey) this.accountKey;
    }

    public int hashCode() {
        return Objects.hash(accountKey, firstName, lastName, balance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount other = (BankAccount) o;
        return Objects.equals(accountKey, other.accountKey)
                && Objects.equals(firstName, other.firstName)
                && Objects.equals(lastName, other.lastName)
                && Objects.equals(balance, other.balance);
    }

    @Override
    public int compareTo(BankAccount o) {
        return Comparator.
                comparing(BankAccount::getBankAccountKey)
                .thenComparing(BankAccount::getFirstName)
                .thenComparing(BankAccount::getLastName)
                .thenComparing(BankAccount::getBalance)
                .compare(this, o);
    }
}
