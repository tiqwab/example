package com.tiqwab.example;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MyUserRepository extends JpaRepository<MyUser, Long> {

    MyUser findByName(String name);

}
