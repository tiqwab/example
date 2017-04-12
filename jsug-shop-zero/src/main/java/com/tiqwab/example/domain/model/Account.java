package com.tiqwab.example.domain.model;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Builder
@Entity
public class Account {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    // Let JPA use field access since @Id annotation is set to a field.
    @Id @GeneratedValue
    private Long Id;
    private String name;
    private String password;
    private String email;
    // TODO: How to store LocalDate properly by JPA?
    private LocalDate birthDay;
    private String zip;
    private String address;
    private Integer age;
    private String[] roles;

    public Account() {

    }

}
