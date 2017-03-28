package com.tiqwab.example.payroll;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Employee {

    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String description;
    // private int age;
    // private Date birthDay;

    @Version
    @JsonIgnore
    private Long version;

    @ManyToOne
    private Manager manager;

    private Employee() {

    }

    public Employee(String firstName, String lastName, String description, Manager manager) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
        this.manager = manager;
    }


}
