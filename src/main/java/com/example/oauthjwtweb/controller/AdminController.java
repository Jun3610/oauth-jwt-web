package com.example.oauthjwtweb.controller;

import com.example.oauthjwtweb.entity.User;
import com.example.oauthjwtweb.service.AdminService; // 또는 UserService 인터페이스
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;  // 스프링 Model 임포트
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService userService;  // UserServiceDeveloper 사용

    public AdminController(AdminService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showUserList(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("현재 사용자 권한 목록: " + auth.getAuthorities()); // ✅ 확인포인트

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/admin";
    }

}