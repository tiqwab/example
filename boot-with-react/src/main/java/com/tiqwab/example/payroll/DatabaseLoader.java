package com.tiqwab.example.payroll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by tiqwab on 3/22/17.
 */
@Component
public class DatabaseLoader implements CommandLineRunner {

    private final EmployeeRepository repository;

    @Autowired
    public DatabaseLoader(EmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        this.repository.save(new Employee("Aiko", "Aida", "this is Aida"));
        this.repository.save(new Employee("Biko", "Bida", "this is Bida"));
        this.repository.save(new Employee("Ciko", "Cida", "this is Cida"));
        this.repository.save(new Employee("Diko", "Dida", "this is Dida"));
        this.repository.save(new Employee("Eiko", "Eida", "this is Eida"));
        this.repository.save(new Employee("Fiko", "Fida", "this is Fida"));
    }

}
