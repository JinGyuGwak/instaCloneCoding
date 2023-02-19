package com.example.demo.src.func;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.NOT_FIND_USER;

@RequiredArgsConstructor
@Service
public class FuncUser {
    private final UserRepository userRepository;

    public User selectUserByIdAndState(Long userId){
        return userRepository.findByIdAndState(userId,ACTIVE)
                .orElseThrow(()->new BaseException(NOT_FIND_USER));
    }
}
