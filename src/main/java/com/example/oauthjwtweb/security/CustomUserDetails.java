package com.example.oauthjwtweb.security;

import com.example.oauthjwtweb.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final User user;
    public CustomUserDetails(User user) {this.user = user;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> {
                    String roleName = role.getRoleName();
                    System.out.println("권한 등록됨: " + roleName);
                    return new SimpleGrantedAuthority("ROLE_" + roleName);
                })
                .collect(Collectors.toList());

        return authorities;
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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  //
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
