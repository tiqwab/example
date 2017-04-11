package com.tiqwab.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyDatabaseLoader implements CommandLineRunner {

    @Autowired
    MyUserRepository myUserRepository;

    @Override
    public void run(String... args) throws Exception {
        myUserRepository.save(new MyUser("user", "user", "ROLE_USER"));
        myUserRepository.save(new MyUser("admin", "admin", "ROLE_ADMIN"));
    }

}
