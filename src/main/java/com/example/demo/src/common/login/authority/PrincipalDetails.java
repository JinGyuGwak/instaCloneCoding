package com.example.demo.src.common.login.authority;

import com.example.demo.src.user.entitiy.User;
import com.example.demo.src.user.enumeration.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
//유저의 정보를 시큐리티에 제공하는 클래스
public class PrincipalDetails implements UserDetails {

    private final User user;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Role role = user.getRole();
        String authority = role.name();

        SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority(authority);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(adminAuthority);

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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
    // 암호 사용 기간에 대한 유효성 검사
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    //특정 사이트 규칙에 따라 return false 로 설정
    public boolean isEnabled() {
        return true;
    }
}