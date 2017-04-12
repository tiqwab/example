package com.tiqwab.example.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@ToString(exclude = "password")
@Entity
public class Account {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    // Let JPA use field access since @Id annotation is set to a field.
    @Id @GeneratedValue
    private Long Id;
    private String name;
    private String password;
    private String email;
    private LocalDate birthDay;
    private String zip;
    private String address;
    private Integer age;

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    public Account() {

    }

    public Account(String name, String password) {
        this.name = name;
        setPassword(password);
    }

    public static AccountBuilder builder() {
        return new AccountBuilder();
    }

    public static class AccountBuilder {

    }

}
