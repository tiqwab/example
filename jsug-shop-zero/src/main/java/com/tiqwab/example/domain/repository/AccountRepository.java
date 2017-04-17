package com.tiqwab.example.domain.repository;

import com.tiqwab.example.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByName(String name);
    Long countByEmail(String email);
}
