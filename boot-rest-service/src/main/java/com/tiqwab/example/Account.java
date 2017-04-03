package com.tiqwab.example;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@ToString(exclude = "password")
@Data
public class Account {

    // public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @OneToMany(mappedBy = "account")
    private Set<Bookmark> bookmarks = new HashSet<>();

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    public void setPassword(String password) {
        // this.password = PASSWORD_ENCODER.encode(password);
        this.password = password;
    }

    // for jpa
    Account() {

    }

    public Account(String name, String password) {
        this.username = name;
        this.setPassword(password);
    }
}
