package com.example.oauthjwtweb.service;

import com.example.oauthjwtweb.entity.User;
import com.example.oauthjwtweb.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceDeveloper {

    private final UserRepository userRepository;

    // 생성자 주입
    public UserServiceDeveloper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 사용자 전체 조회 메서드
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
