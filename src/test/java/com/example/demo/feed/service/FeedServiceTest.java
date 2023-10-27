package com.example.demo.feed.service;


import com.example.demo.src.feed.dto.FeedDto;
import com.example.demo.src.feed.entitiy.FeedContent;
import com.example.demo.src.feed.repository.FeedRepository;
import com.example.demo.src.feed.service.FeedService;
import com.example.demo.src.user.entitiy.User;
import com.example.demo.src.user.repository.UserRepository;
import com.example.demo.src.user.service.UserService;
import com.example.demo.src.util.S3Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class FeedServiceTest {
    @Autowired
    private FeedService feedService;
    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private S3Uploader s3Uploader;

    FeedContent dummyFeedContent1(){
        return new FeedContent("카리나","test1.com",10L);
    }
    FeedContent dummyFeedContent2(){
        return new FeedContent("고마츠 나나","test2.com",10L);
    }

    private static MockMultipartFile getMockMultipart1() throws IOException {
        InputStream inputStream = new ClassPathResource("").getInputStream();
        MockMultipartFile file = new MockMultipartFile("file1", "saveOriginalName.png",
                "image/png", inputStream);
        return file;
    }
    private static MockMultipartFile getMockMultipart2() throws IOException {
        InputStream inputStream = new ClassPathResource("").getInputStream();
        MockMultipartFile file = new MockMultipartFile("file2", "saveOriginalName.png",
                "image/png", inputStream);
        return file;
    }

    @BeforeEach
    public void be() throws Exception{
        userRepository.save((User.builder()
                    .email("test2695@naver.com")
                    .name("고마츠 나나")
                    .password("269599zz")
                    .build()));
        upload_test();
    }
    @Test
    public void upload_test() throws Exception{
        List<FeedContent> list = new ArrayList<>();
        list.add(dummyFeedContent1());
        list.add(dummyFeedContent2());
        given(s3Uploader.uploadFiles(any(),anyString())).willReturn(list);

        List<MultipartFile> files = new ArrayList<>();
        files.add(getMockMultipart1());
        files.add(getMockMultipart2());

        User user1 = userRepository.findByEmail("test2695@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("유저에러"));

        FeedDto.PostFeedRes dummy = feedService.upload(user1.getId(), files, "카리나는 신이야");
//        assertThat(dummy.getFeedId()).isEqualTo(1L);
        assertThat(dummy.getUserId()).isEqualTo(user1.getId());
    }
    @Test
    public void updateFeed_test() throws Exception{
        FeedDto.UpdateFeedDto dummyUpdate =
                FeedDto.UpdateFeedDto.builder()
                    .updateText("카리나는 신이었어")
                    .feedId(feedRepository.findByPostText("카리나는 신이야").getId())
                    .build();

        FeedDto.UpdateFeedDto dto = feedService.updateFeed(dummyUpdate);
        assertThat(dto.getUpdateText()).isEqualTo("카리나는 신이었어");
    }
    @Test
    public void searchUserFeed_test(){
        User user1 = userRepository.findByEmail("test2695@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("유저에러"));
        List<FeedDto.GetFeedRes> getFeedRes = feedService.searchUserFeed(user1.getId());
        assertThat(getFeedRes.get(0).getFeedText()).isEqualTo("카리나는 신이야");
        assertThat(getFeedRes.get(0).getUsername()).isEqualTo("고마츠 나나");
        assertThat(getFeedRes.get(0).getFeedContentListURL().size()).isEqualTo(2);
        assertThat(getFeedRes.get(0).getFeedContentListURL().get(0)).isEqualTo("test1.com");
    }
    @Test
    public void searchAllFeed_test(){
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<FeedDto.GetFeedRes> res = feedService.searchAllFeed(pageRequest);
        assertThat(res.size()).isEqualTo(1);
        assertThat(res.get(0).getFeedText()).isEqualTo("카리나는 신이야");
        assertThat(res.get(0).getUsername()).isEqualTo("고마츠 나나");
        assertThat(res.get(0).getFeedContentListURL().size()).isEqualTo(2);
        assertThat(res.get(0).getFeedContentListURL().get(0)).isEqualTo("test1.com");
    }



}
