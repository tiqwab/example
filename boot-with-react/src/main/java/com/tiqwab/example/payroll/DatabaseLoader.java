package com.tiqwab.example.payroll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by tiqwab on 3/22/17.
 */
@Component
public class DatabaseLoader implements CommandLineRunner {

    private final EmployeeRepository employees;
    private final ManagerRepository managers;

    @Autowired
    public DatabaseLoader(EmployeeRepository employeeRepository, ManagerRepository managerRepository) {
        this.employees = employeeRepository;
        this.managers = managerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Manager greg = this.managers.save(new Manager("greg", "turnquist", "ROLE_MANAGER"));
        Manager oliver = this.managers.save(new Manager("oliver", "gierke", "ROLE_MANAGER"));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("greg", "doesn't matter",
                        AuthorityUtils.createAuthorityList("ROLE_MANAGER")));

        this.employees.save(new Employee("Aiko", "Aida", "this is Aida", greg));
        this.employees.save(new Employee("Biko", "Bida", "this is Bida", greg));
        this.employees.save(new Employee("Ciko", "Cida", "this is Cida", greg));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("oliver", "doesn't matter",
                        AuthorityUtils.createAuthorityList("ROLE_MANAGER")));

        this.employees.save(new Employee("Diko", "Dida", "this is Dida", oliver));
        this.employees.save(new Employee("Eiko", "Eida", "this is Eida", oliver));
        this.employees.save(new Employee("Fiko", "Fida", "this is Fida", oliver));

        SecurityContextHolder.clearContext();
    }

}
