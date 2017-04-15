package com.tiqwab.example.app;

import com.tiqwab.example.domain.validation.Confirm;
import com.tiqwab.example.domain.validation.UnusedEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Confirm(field = "password")
public class AccountForm {

    @NotNull @Size(min = 1, max = 40)
    private String name;
    @NotNull @Size(min = 6)
    private String password;
    @NotNull
    private String confirmPassword;
    @Email @Size(min = 1, max = 100) @NotNull @UnusedEmail
    private String email;
    // Use 'pattern' attribute if the more flexible pattern is necessary.
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull
    private LocalDate birthDay;
    @NotNull @Pattern(regexp = "[0-9]{7}")
    private String zip;
    @NotNull @Size(min = 1, max = 100)
    private String address;
    @NotNull @Min(0) @Max(200)
    private Integer age;
    private String[] roles;

}
