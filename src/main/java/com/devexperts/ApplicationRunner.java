package com.devexperts;

import com.devexperts.model.service.AccountService;
import com.devexperts.repository.AccountRepository;
import com.devexperts.service.AccountServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationRunner.class, args);
    }

    @Bean
    AccountService accountService() {
        return new AccountServiceImpl(new AccountRepository());
    }
}