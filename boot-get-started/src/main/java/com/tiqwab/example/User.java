package com.tiqwab.example;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by nm on 3/15/17.
 */
@Data
@Entity
public class User {

    @Id
    @GeneratedValue
    private Integer id;
    private String firstName;
    private String lastName;

    private User() {

    }

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
