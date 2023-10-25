package com.example.demo.followMapping.service;

import com.example.demo.src.followMapping.dto.FollowMappingDto;
import com.example.demo.src.followMapping.repository.FollowMappingRepository;
import com.example.demo.src.followMapping.service.FollowService;
import com.example.demo.src.user.entitiy.User;
import com.example.demo.src.user.repository.UserRepository;
import com.example.demo.src.user.service.UserService;
import com.example.demo.src.util.LoginUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class FollowServiceTest {
    @Autowired
    private FollowService followService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowMappingRepository followMappingRepository;
    @Autowired
    private UserService userService;

    User dummyUser1(){
        return User.builder()
                .email("test1@naver.com")
                .name("카리나")
                .password("269599zz")
                .build();
    }
    User dummyUser2(){
        return User.builder()
                .email("test2@naver.com")
                .name("고마츠 나나")
                .password("269599zz")
                .build();
    }

    FollowMappingDto.FollowDto dummyFollowDto(){
        User user1 = userRepository.findByEmail("test1@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("유저에러"));
        User user2 = userRepository.findByEmail("test2@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("유저에러"));

        return FollowMappingDto.FollowDto.builder()
                .followUserId(user2.getId())
                .loginUserEmail(user1.getEmail())
                .build();
    }


}
