package com.tiqwab.example.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountForm {

    private String name;
    private String password;
    private String confirmPassword;
    private String email;
    private LocalDate birthDay;
    private String zip;
    private String address;
    private Integer age;
    private String[] roles;

}
