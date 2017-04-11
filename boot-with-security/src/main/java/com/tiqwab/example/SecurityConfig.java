package com.tiqwab.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/js/**", "/css/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/", false)
                    .permitAll()
                    .and()
                .httpBasic()
                    .and()
                // .csrf()
                //     .disable()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/");
    }

    // Reference: http://ziqoo.com/wiki/index.php?Spring%20Security%20%A5%C7%A1%BC%A5%BF%A5%D9%A1%BC%A5%B9%A4%F2%BB%C8%CD%D1%A4%B7%A4%BF%C7%A7%BE%DA
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                // Set up user data in memory
                /*
                .inMemoryAuthentication()
                    .withUser("user").password("user").roles("USER")
                    .and()
                    .withUser("admin").password("admin").roles("ADMIN")
                */
                // Set up user data in database
                /*
                .jdbcAuthentication()
                    .dataSource(dataSource)
                    .withDefaultSchema()
                    .withUser("user").password("user").roles("USER")
                    .and()
                    .withUser("admin").password("admin").roles("ADMIN")
                */
                .userDetailsService(this.myUserDetailsService)
                .passwordEncoder(MyUser.PASSWORD_ENCODER)
                ;
    }

    // This is the configuration for global AuthenticationManager (https://spring.io/guides/topicals/spring-security-architecture/)
    // I am not sure which should be used...
    /*
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("user").roles("USER")
                .and()
                .withUser("admin").password("admin").roles("ADMIN")
        ;

    }
    */

}
