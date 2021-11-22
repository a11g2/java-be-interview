package com.devexperts.rest;

import com.devexperts.model.account.Account;
import com.devexperts.model.rest.AbstractAccountController;
import com.devexperts.model.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AccountController implements AbstractAccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping("/operations/transfer/{source_id}/{target_id}/{amount}")
    public ResponseEntity<Void> transfer(@PathVariable("source_id") long sourceId,
                                         @PathVariable("target_id") long targetId,
                                         @PathVariable("amount") double amount) {
        Account source = accountService.getAccount(sourceId);
        Account target = accountService.getAccount(targetId);
        accountService.transfer(source, target, amount);
        return ResponseEntity.of(null);

    }
}
