package com.example.oauthjwtweb.controller;

import com.example.oauthjwtweb.entity.User;
import com.example.oauthjwtweb.service.UserServiceDeveloper; // 또는 UserService 인터페이스
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;  // 스프링 Model 임포트
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserServiceDeveloper userService;  // UserServiceDeveloper 사용

    public AdminController(UserServiceDeveloper userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showUserList(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin";  // admin.html 뷰 이름
    }
}
