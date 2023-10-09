package com.example.demo.src.util;

import com.example.demo.src.user.repository.UserRepository;
import com.example.demo.src.user.entitiy.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.demo.src.common.entity.BaseEntity.State.ACTIVE;

@RequiredArgsConstructor
@Service
public class FuncUser {
    private final UserRepository userRepository;

    public User findUserByIdAndState(Long userId){
        return userRepository.findByIdAndState(userId,ACTIVE)
                .orElseThrow(()->new IllegalArgumentException("일치하는 유저가 없습니다."));
    }
    public User findUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("일치하는 유저가 없습니다."));
    }
    public User findUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("일치하는 유저가 없습니다."));
    }

}
