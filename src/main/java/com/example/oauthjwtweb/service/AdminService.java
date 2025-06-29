package com.example.oauthjwtweb.service;

import com.example.oauthjwtweb.entity.User;
import com.example.oauthjwtweb.repository.adminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final adminRepository userRepository;

    @Autowired
    public AdminService(adminRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
