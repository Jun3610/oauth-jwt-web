package com.example.oauthjwtweb.service;

import com.example.oauthjwtweb.entity.User;
import com.example.oauthjwtweb.repository.adminRepository;
import com.example.oauthjwtweb.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final adminRepository userRepository;

    @Autowired
    public CustomUserDetailsService(adminRepository userRepository){this.userRepository = userRepository;}

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
            User user = userRepository.findByUserId(userId).orElseThrow( // if value is null
            () -> new UsernameNotFoundException("User not found with userId: " + userId)); // Default Value
        return new CustomUserDetails(user); //Returning UserDetails Type
    }
}
