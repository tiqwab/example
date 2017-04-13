package com.tiqwab.example.domain.service;

import com.tiqwab.example.domain.model.Account;
import com.tiqwab.example.domain.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public boolean isUnusedEmail(String email) {
        return accountRepository.countByEmail(email) == 0;
    }

    public Account register(Account account) {
        return accountRepository.save(account);
    }

}
