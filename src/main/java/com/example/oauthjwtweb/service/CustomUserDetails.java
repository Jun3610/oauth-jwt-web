package com.example.oauthjwtweb.service;

import com.example.oauthjwtweb.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final User user;
    public CustomUserDetails(User user) {this.user = user;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // 필요하면 구현
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // 필요하면 구현
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // 필요하면 구현
    }

    @Override
    public boolean isEnabled() {
        return true;  // 필요하면 구현
    }
}
