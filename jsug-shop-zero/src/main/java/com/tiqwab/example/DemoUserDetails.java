package com.tiqwab.example;

import com.tiqwab.example.domain.model.Account;
import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class DemoUserDetails extends User {

    @Getter
    private Account account;

    public DemoUserDetails(Account account) {
        super(account.getName(), account.getPassword(), AuthorityUtils.createAuthorityList(account.getRoles()));
        this.account = account;
    }

}
