package com.tiqwab.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    MyUserRepository myUserRepository;

    @Autowired
    MyUserRoleRepository myUserRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser myUser = myUserRepository.findByName(username);
        if (myUser == null) {
            throw new UsernameNotFoundException("");
        }
        return new User(myUser.getName(), myUser.getPassword(), AuthorityUtils.createAuthorityList(
                myUserRoleRepository.findByUser(myUser).stream().map(x -> x.getRole()).toArray(String[]::new)
        ));
    }

}
