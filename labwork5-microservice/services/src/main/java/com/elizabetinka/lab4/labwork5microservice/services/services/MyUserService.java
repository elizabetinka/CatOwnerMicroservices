package com.elizabetinka.lab4.labwork5microservice.services.services;


import com.elizabetinka.lab4.dto.*;
import com.elizabetinka.lab4.labwork5microservice.services.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("were");
        return new MyUserDetails(userService.GetUserByUsername(username));
    }
}
