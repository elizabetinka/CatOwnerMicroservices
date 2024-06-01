package com.elizabetinka.lab4.labwork5microservice.services;
import com.elizabetinka.lab4.dto.UserDto;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {
    private UserDto user;

    public MyUserDetails(UserDto user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("chain");
        if (user == null){
            return null;
        }
        return Arrays.stream(user.getRoles().split(", "))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        System.out.println("chain");
        if (user == null){
            return null;
        }
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        System.out.println("chain");
        if (user == null){
            return null;
        }
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

