package com.tiqwab.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class MyDatabaseLoader implements CommandLineRunner {

    @Autowired
    MyUserRepository myUserRepository;

    @Autowired
    MyUserRoleRepository myUserRoleRepository;

    @Override
    public void run(String... args) throws Exception {
        MyUser user = new MyUser("user", "user");
        MyUser admin = new MyUser("admin", "admin");
        myUserRepository.save(user);
        myUserRepository.save(admin);
        myUserRoleRepository.save(new MyUserRole(user, "ROLE_USER"));
        myUserRoleRepository.save(new MyUserRole(admin, "ROLE_ADMIN"));
    }

}
