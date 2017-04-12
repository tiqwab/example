package com.tiqwab.example;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity
public class MyUserRole {

    @Id @GeneratedValue
    private Long id;
    @ManyToOne
    private MyUser user;
    private String role;

    public MyUserRole() {

    }

    public MyUserRole(MyUser user, String role) {
        this.user = user;
        this.role = role;
    }
}
