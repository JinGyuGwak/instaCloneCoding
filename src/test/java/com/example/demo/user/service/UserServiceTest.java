package com.example.demo.user.service;

import com.example.demo.src.user.dto.UserDto;
import com.example.demo.src.user.entitiy.User;
import com.example.demo.src.user.enumeration.Role;
import com.example.demo.src.user.repository.UserRepository;
import com.example.demo.src.user.service.UserService;
import com.example.demo.src.util.FuncUser;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.demo.src.common.entity.BaseEntity.State.ACTIVE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FuncUser funcUser;
    UserDto.PostUserDto dummyPostUserDto(){
        return UserDto.PostUserDto.builder()
                .email("test2695@naver.com")
                .name("카리나")
                .password("269599zz")
                .build();
    }
    @BeforeEach
    void before(){
        userService.createUser(dummyPostUserDto());
    }
    @Test
    public void createUser_test(){
        //when
        User user = funcUser.findUserByEmail("test2695@naver.com");
        //then
        assertThat(user.getEmail()).isEqualTo("test2695@naver.com");
        assertThat(user.getName()).isEqualTo("카리나");
    }
    @Test
    public void modifyUserName_test(){
        //given
        User userByEmail = funcUser.findUserByEmail("test2695@naver.com");
        userService.modifyUserName(userByEmail.getId(),"카리나는 신이야");
        //when
        User user = funcUser.findUserByEmail("test2695@naver.com");
        //then
        assertThat(user.getEmail()).isEqualTo("test2695@naver.com");
        assertThat(user.getName()).isEqualTo("카리나는 신이야");
    }



}
