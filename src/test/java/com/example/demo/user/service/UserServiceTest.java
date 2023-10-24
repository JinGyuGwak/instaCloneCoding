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

import java.util.List;
import java.util.Optional;

import static com.example.demo.src.common.entity.BaseEntity.State.ACTIVE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    UserDto.PostUserDto dummyPostUserDto1(){
        return UserDto.PostUserDto.builder()
                .email("test1111@naver.com")
                .name("고마츠나나")
                .password("269599zz")
                .build();
    }
    @BeforeEach
    void before(){
        userService.createUser(dummyPostUserDto());
        userService.createUser(dummyPostUserDto1());
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
    @Test
    public void deleteUser_test(){
        //given
        User userByEmail = funcUser.findUserByEmail("test2695@naver.com");
        userService.deleteUser(userByEmail.getId());
        //then
        assertThrows(IllegalArgumentException.class, () -> {
            funcUser.findUserByIdAndState(userByEmail.getId());
        });
    }
    @Test
    public void getUsers_test(){
        //given
        List<UserDto.GetUserRes> users = userService.getUsers();
        //then
        assertThat(users.size()).isEqualTo(2);
    }
    @Test
    public void getUsersByEmail_test(){
        //given
        List<UserDto.GetUserRes> usersByEmail = userService.getUsersByEmail("test2695@naver.com");
        //when
        UserDto.GetUserRes getUserRes = usersByEmail.get(0);
        //then
        assertThat(usersByEmail.size()).isEqualTo(1);
        assertThat(getUserRes.getEmail()).isEqualTo("test2695@naver.com");
        assertThat(getUserRes.getName()).isEqualTo("카리나");
    }
    @Test
    public void logIn_tesT(){
        //given
        UserDto.LoginUserDto loginUserDto = userService.logIn("test2695@naver.com", "269599zz");

        assertThat(loginUserDto.getEmail()).isEqualTo("test2695@naver.com");
        assertThat(loginUserDto.getPassword()).isEqualTo("269599zz");
        assertNotNull(loginUserDto.getJwt());
    }



}
