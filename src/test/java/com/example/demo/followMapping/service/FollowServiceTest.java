package com.example.demo.followMapping.service;

import com.example.demo.src.followMapping.dto.FollowMappingDto;
import com.example.demo.src.followMapping.entitiy.FollowMapping;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @BeforeEach
    void before(){
        User user1 = userRepository.save(dummyUser1());
        User user2 = userRepository.save(dummyUser2());
        followService.followUser(user1.getId(), user2.getId());
    }
    @Test
    @WithMockUser(username = "test2@naver.com")
    public void followSearch_test(){ //나나가 카리나한테 요청
        User user1 = userRepository.findByEmail("test1@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("유저에러"));
        User user2 = userRepository.findByEmail("test2@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("유저에러"));
        List<FollowMappingDto.FollowDto> followDto = followService.followSearch(user2.getId());

        assertThat(followDto.size()).isEqualTo(1);
        assertThat(followDto.get(0).getLoginUserEmail()).isEqualTo("test2@naver.com");
        assertThat(followDto.get(0).getFollowUserId()).isEqualTo(user1.getId());
    }
    @Test
    @WithMockUser(username = "test1@naver.com")
    public void followerSearch_test(){
        User user1 = userRepository.findByEmail("test1@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("유저에러"));
        User user2 = userRepository.findByEmail("test2@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("유저에러"));
        List<FollowMappingDto.FollowerDto> followerDto = followService.followerSearch(user1.getId());

        assertThat(followerDto.size()).isEqualTo(1);
        assertThat(followerDto.get(0).getLoginUserEmail()).isEqualTo("test1@naver.com");
        assertThat(followerDto.get(0).getFollowerUserId()).isEqualTo(user2.getId());
    }
    @Test
    public void followUser_test(){
        User user1 = userRepository.findByEmail("test1@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("유저에러"));
        User user2 = userRepository.findByEmail("test2@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("유저에러"));
        FollowMapping followMapping =
                followMappingRepository.findByFollowUserIdAndFollowerUserId(user1.getId(), user2.getId())
                        .orElseThrow(()-> new IllegalArgumentException("테스트가 실패하였습니다."));
        assertThat(followMapping.getFollowUser().getEmail()).isEqualTo("test1@naver.com");
        assertThat(followMapping.getFollowUser().getName()).isEqualTo("카리나");
        assertThat(followMapping.getFollowerUser().getEmail()).isEqualTo("test2@naver.com");
        assertThat(followMapping.getFollowerUser().getName()).isEqualTo("고마츠 나나");
    }
    @Test
    public void followDelete_test(){
        User user1 = userRepository.findByEmail("test1@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("유저에러"));
        User user2 = userRepository.findByEmail("test2@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("유저에러"));
        followService.followDelete(user1.getId(), user2.getId());

        assertThrows(IllegalArgumentException.class, () -> {
            followMappingRepository.
                    findByFollowUserIdAndFollowerUserId(user1.getId(), user2.getId())
                    .orElseThrow(()->new IllegalArgumentException("일치하는 유저가 없습니다."));
        });


    }
}
