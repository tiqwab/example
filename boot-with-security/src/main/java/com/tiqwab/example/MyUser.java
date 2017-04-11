package com.tiqwab.example;

import lombok.Data;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@ToString(exclude = "password")
@Entity
public class MyUser {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Id @GeneratedValue
    private Long id;
    private String name;
    private String password;
    private String[] roles;

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    public MyUser() {

    }

    public MyUser(String name, String password, String... roles) {
        this.name = name;
        this.setPassword(password);
        this.roles = roles;
    }

}
