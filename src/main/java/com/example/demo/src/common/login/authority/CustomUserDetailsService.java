package com.example.demo.src.common.login.authority;

import com.example.demo.src.user.entitiy.User;
import com.example.demo.src.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//UserDetailsService 는 스프링 시큐리티에서 사용자 정보를 로드하기 위한 인터페이스
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    // 해당 메소드는 사용자 이름(일반적으로는 사용자의 식별자인 username 또는 email)을 입력받아 사용자 정보를 조회하여 UserDetails 인터페이스를 반환해야 합니다.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("회원정보를 찾을 수 없습니다."));

        return new PrincipalDetails(user);
    }
}
