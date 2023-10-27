package com.example.demo.myPage.service;

import com.example.demo.src.myPage.dto.MyPageDto;
import com.example.demo.src.myPage.entitiy.MyPage;
import com.example.demo.src.myPage.repository.MyPageRepository;
import com.example.demo.src.myPage.service.MyPageService;
import com.example.demo.src.user.entitiy.User;
import com.example.demo.src.user.repository.UserRepository;
import com.example.demo.src.user.service.UserService;
import com.example.demo.src.util.LoginUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MyPageServiceTest {
    @Autowired
    private MyPageService myPageService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MyPageRepository myPageRepository;

    User dummyUser(){
        return User.builder()
                .email("test2695@naver.com")
                .name("고마츠 나나")
                .password("269599zz")
                .build();
    }
    MyPageDto dummyMyPageDto(){
        return MyPageDto.builder()
                .userId(userRepository.findByEmail("test2695@naver.com").orElseThrow(
                        () -> new IllegalArgumentException("일치하는 유저가 없습니다.")).getId())
                .name("고마츠 나나")
                .nickname("나나는 신이야")
                .introduction("카리나!카리나!")
                .build();
    }
    @BeforeEach
    void before(){
        userRepository.save(dummyUser());
        myPageService.createMyPage(dummyMyPageDto());
    }

    @Test
    public void createMyPage_test(){
        //when
        MyPage myPage = myPageRepository.findByName("고마츠 나나")
                .orElseThrow(() -> new IllegalArgumentException("DB 연결에 실패한 테스트입니다."));
        //then
        assertThat(myPage.getName()).isEqualTo("고마츠 나나");
        assertThat(myPage.getNickname()).isEqualTo("나나는 신이야");
        assertThat(myPage.getIntroduction()).isEqualTo("카리나!카리나!");
    }
    @Test
    public void updateMyPage_test(){
        //when
        MyPage myPage = myPageRepository.findByName("고마츠 나나")
                .orElseThrow(() -> new IllegalArgumentException("DB 연결에 실패한 테스트입니다."));
        User user1 = userRepository.findByEmail("test2695@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        myPageService.updateMyPage(
                MyPageDto.builder()
                        .userId(user1.getId())
                        .name("카리나")
                        .nickname("카리나는 신이야")
                        .introduction("고마츠 나나!")
                        .build());
        //then
        MyPage myPage2 = myPageRepository.findByName("카리나")
                .orElseThrow(() -> new IllegalArgumentException("DB 연결에 실패한 테스트입니다."));
        assertThat(myPage2.getName()).isEqualTo("카리나");
        assertThat(myPage2.getNickname()).isEqualTo("카리나는 신이야");
        assertThat(myPage2.getIntroduction()).isEqualTo("고마츠 나나!");
    }
    @Test
    public void getMyPage_test(){
        //when
        User user1 = userRepository.findByEmail("test2695@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        MyPageDto myPageDto = myPageService.getMyPage(user1.getId());
        assertThat(myPageDto.getName()).isEqualTo("고마츠 나나");
        assertThat(myPageDto.getNickname()).isEqualTo("나나는 신이야");
        assertThat(myPageDto.getIntroduction()).isEqualTo("카리나!카리나!");
    }


}
