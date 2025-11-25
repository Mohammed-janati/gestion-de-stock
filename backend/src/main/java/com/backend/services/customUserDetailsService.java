package com.backend.services;

import com.backend.repository.userRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

public class customUserDetailsService implements UserDetailsService {


    private final userRepo repo;

    public customUserDetailsService(userRepo repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var a=repo.findByUsername(username);
        if(a==null) throw new UsernameNotFoundException(username);
        return User.builder()
                .username(a.getUsername())
                .password(a.getPassword())
                .authorities(Collections.emptyList())
                .build();

    }
}
