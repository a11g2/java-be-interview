package test;

import com.devexperts.exception.AccountNotFoundException;
import com.devexperts.exception.BalanceNotEnoughException;
import com.devexperts.exception.TransferFailedException;
import com.devexperts.model.account.Account;
import com.devexperts.model.account.BankAccount;
import com.devexperts.model.account.BankAccountKey;
import com.devexperts.model.service.AccountService;
import com.devexperts.repository.AccountRepository;
import com.devexperts.service.AccountServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;


@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;

    @Test
    public void accountGetSuccessTest(){
        //given
        AccountService accountService = new AccountServiceImpl(accountRepository);
        BankAccountKey accountKey = BankAccountKey.valueOf(1);
        BankAccount bankAccount = new BankAccount(accountKey, "firstName", "secondName", 18.0);

        //when
        Mockito.when(accountRepository.getAccount(any())).thenReturn(bankAccount);
        Account accountResp = accountService.getAccount(1);

        //then
        Assert.assertNotNull(accountResp);
        assertEquals(bankAccount, accountResp);

    }

    @Test
    public void accountGetFailedTest() {
        //given
        AccountService accountService = new AccountServiceImpl(accountRepository);

        //when
        Mockito.when(accountRepository.getAccount(any())).thenReturn(null);

        //then
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccount(15));

    }

    @Test
    public void transferSuccess(){
        //given
        AccountService accountService = new AccountServiceImpl(accountRepository);
        BankAccountKey sourceKey = BankAccountKey.valueOf(1);
        BankAccount source = new BankAccount(sourceKey, "firstName", "secondName", 20.0);
        BankAccountKey targetKey = BankAccountKey.valueOf(2);
        BankAccount target = new BankAccount(targetKey, "firstName", "secondName", 18.0);

        //when
        accountService.transfer(source, target, 20.0);

        //then
        Mockito.verify(accountRepository).updateAccount(source);
        Mockito.verify(accountRepository).updateAccount(target);
        Mockito.verifyNoMoreInteractions(accountRepository);
    }

    @Test
    public void transferFailedAmountNotEnough(){
        //given
        AccountService accountService = new AccountServiceImpl(accountRepository);
        BankAccountKey sourceKey = BankAccountKey.valueOf(1);
        BankAccount source = new BankAccount(sourceKey, "firstName", "secondName", 19.0);
        BankAccountKey targetKey = BankAccountKey.valueOf(2);
        BankAccount target = new BankAccount(targetKey, "firstName", "secondName", 18.0);

        //when
        assertThrows(BalanceNotEnoughException.class, () -> accountService.transfer(source, target, 20.0));

        //then
        Mockito.verifyNoMoreInteractions(accountRepository);
    }


    @Test
    public void transferFailedOnSource(){
        //given
        AccountService accountService = new AccountServiceImpl(accountRepository);
        BankAccountKey sourceKey = BankAccountKey.valueOf(1);
        BankAccount source = new BankAccount(sourceKey, "firstName", "secondName", 20.0);
        BankAccountKey targetKey = BankAccountKey.valueOf(2);
        BankAccount target = new BankAccount(targetKey, "firstName", "secondName", 18.0);

        //when
        Mockito.doThrow(new TransferFailedException()).when(accountRepository).updateAccount(source);

        //then
        assertThrows(TransferFailedException.class, () -> accountService.transfer(source, target, 20.0));
        Mockito.verify(accountRepository).updateAccount(source);
        Mockito.verifyNoMoreInteractions(accountRepository);
    }


    @Test
    public void transferFailedOnTargetRollbackTransaction(){
        //given
        AccountService accountService = new AccountServiceImpl(accountRepository);
        BankAccountKey sourceKey = BankAccountKey.valueOf(1);
        BankAccount source = new BankAccount(sourceKey, "firstName", "secondName", 20.0);
        BankAccountKey targetKey = BankAccountKey.valueOf(2);
        BankAccount target = new BankAccount(targetKey, "firstName", "secondName", 18.0);

        //when
        Mockito.doThrow(new TransferFailedException()).when(accountRepository).updateAccount(target);

        //then
        assertThrows(TransferFailedException.class, () -> accountService.transfer(source, target, 20.0));
        Mockito.verify(accountRepository, Mockito.times(2)).updateAccount(source);
        Mockito.verify(accountRepository).updateAccount(target);

        Mockito.verifyNoMoreInteractions(accountRepository);
    }
}
