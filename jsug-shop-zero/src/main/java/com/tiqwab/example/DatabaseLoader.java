package com.tiqwab.example;

import com.tiqwab.example.domain.model.Account;
import com.tiqwab.example.domain.repository.AccountRepository;
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
        final Account user = Account.builder()
                .name("user")
                .password(Account.PASSWORD_ENCODER.encode("user"))
                .email("user@user.com")
                .birthDay(LocalDate.of(2017, 4, 1))
                .zip("111-1111")
                .address("Tokyo")
                .age(20)
                .roles(new String[]{"ROLE_USER"})
                .build();
        accountRepository.save(user);
    }
}
