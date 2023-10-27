package com.example.demo.feed.service;

import com.example.demo.src.feed.dto.FeedLikeDto;
import com.example.demo.src.feed.entitiy.Feed;
import com.example.demo.src.feed.entitiy.FeedLike;
import com.example.demo.src.feed.repository.FeedLikeRepository;
import com.example.demo.src.feed.repository.FeedRepository;
import com.example.demo.src.feed.service.FeedLikeService;
import com.example.demo.src.user.entitiy.User;
import com.example.demo.src.user.repository.UserRepository;
import com.example.demo.src.util.FuncFeed;
import com.example.demo.src.util.FuncUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class FeedLikeServiceTest {
    @Autowired
    private FuncUser funcUser;
    @Autowired
    private FuncFeed funcFeed;
    @Autowired
    private FeedLikeRepository feedLikeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private FeedLikeService feedLikeService;
    Feed dummyFeed(User user){
        return Feed.builder()
                .user(user)
                .postText("나나는신이야")
                .reportCount(0)
                .build();


    }
    @BeforeEach
    void before(){
        User user = userRepository.save((User.builder()
                .email("test2695@naver.com")
                .name("고마츠 나나")
                .password("269599zz")
                .build()));
        feedRepository.save(dummyFeed(user));
    }
    @Test
    public void userLikeFeed_test() throws Exception{
        User user = userRepository.findByEmail("test2695@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));
        Feed feed = feedRepository.findByPostText("나나는신이야");

        FeedLikeDto feedLikeDto =
                feedLikeService.userLikeFeed(FeedLikeDto.builder()
                        .userId(user.getId())
                        .feedId(feed.getId())
                        .build());
        assertThat(feedLikeDto.getFeedId()).isEqualTo(feed.getId());
        assertThat(feedLikeDto.getUserId()).isEqualTo(user.getId());
    }
    @Test
    public void feedLikeDelete_test(){
        User user = userRepository.findByEmail("test2695@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));
        Feed feed = feedRepository.findByPostText("나나는신이야");

        feedLikeRepository.save(FeedLike.builder()
                        .feed(feed)
                        .user(user)
                        .build());
        feedLikeService.feedLikeDelete(FeedLikeDto.builder()
                        .feedId(feed.getId())
                        .userId(user.getId())
                        .build());

        assertThrows(IllegalArgumentException.class, () -> {
            feedLikeRepository
                    .findByFeedIdAndUserId(feed.getId(), user.getId())
                    .orElseThrow(()->new IllegalArgumentException("카리나는 진짜 왜이렇게 이쁜걸까"));
        });
    }
    @Test
    public void feedLikeSearch_test(){
        Feed feed = feedRepository.findByPostText("나나는신이야");
        User user = userRepository.findByEmail("test2695@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));

        feedLikeRepository.save(FeedLike.builder()
                .feed(feed)
                .user(user)
                .build());
        List<FeedLikeDto.GetFeedLikeRes> list = feedLikeService.feedLikeSearch(feed.getId());
        FeedLikeDto.GetFeedLikeRes getFeedLikeRes = list.get(0);

        assertThat(list.size()).isEqualTo(1);
        assertThat(getFeedLikeRes.getUserEmail()).isEqualTo("test2695@naver.com");


    }

}
