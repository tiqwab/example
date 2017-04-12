package com.tiqwab.example;

import com.tiqwab.example.domain.Account;
import com.tiqwab.example.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseLoader.class);

    @Autowired
    AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
        final Account user = new Account("user", "user");
        accountRepository.save(user);
    }
}
