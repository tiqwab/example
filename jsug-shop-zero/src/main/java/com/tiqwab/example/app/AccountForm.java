package com.tiqwab.example.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountForm {

    private String name;
    private String password;
    private String confirmPassword;
    private String email;
    // TODO: @DataTimeFormat specifies format of date
    // Use 'pattern' attribute if the more flexible pattern is necessary.
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDay;
    private String zip;
    private String address;
    private Integer age;
    private String[] roles;

}
