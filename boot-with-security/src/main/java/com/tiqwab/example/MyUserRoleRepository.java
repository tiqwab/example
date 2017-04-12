package com.tiqwab.example;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface MyUserRoleRepository extends JpaRepository<MyUserRole, Long> {

    Set<MyUserRole> findByUser(MyUser user);

}
