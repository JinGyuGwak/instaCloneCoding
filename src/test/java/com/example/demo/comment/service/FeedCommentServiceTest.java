package com.example.demo.comment.service;

import com.example.demo.src.comment.dto.CommentDto;
import com.example.demo.src.comment.entity.FeedComment;
import com.example.demo.src.comment.repository.CommentLikeRepository;
import com.example.demo.src.comment.repository.FeedCommentRepository;
import com.example.demo.src.comment.service.CommentLikeService;
import com.example.demo.src.comment.service.FeedCommentService;
import com.example.demo.src.feed.dto.FeedDto;
import com.example.demo.src.feed.entitiy.Feed;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.src.common.entity.BaseEntity.State.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class FeedCommentServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FeedService feedService;
    @Autowired
    private FeedCommentService feedCommentService;
    @Autowired
    private FeedCommentRepository feedCommentRepository;
    @Autowired
    private FeedRepository feedRepository;
    @MockBean
    private S3Uploader s3Uploader;
    @MockBean
    private UserService userService;

    private static MockMultipartFile getMockMultipart1() throws IOException {
        InputStream inputStream = new ClassPathResource("").getInputStream();
        MockMultipartFile file = new MockMultipartFile("file1", "saveOriginalName.png",
                "image/png", inputStream);
        return file;
    }
    FeedContent dummyFeedContent1(){
        return FeedContent.builder()
                .postName("카리나")
                .postURL("test1.com")
                .fileSize(10L)
                .build();
    }
    @BeforeEach
    public void before() throws Exception{
        User user1 = User.builder()
                .email("test1234@naver.com")
                .name("고마츠나나")
                .password("269599zz")
                .build();

        List<FeedContent> list = new ArrayList<>();
        list.add(dummyFeedContent1());
        given(s3Uploader.uploadFiles(any(),anyString())).willReturn(list);

        List<MultipartFile> files = new ArrayList<>();
        files.add(getMockMultipart1());

        User user = userRepository.save(user1);
        given(userService.searchUserById(anyLong())).willReturn(user);

        feedService.upload(user.getId(), files, "카리나는 신이야");

        feedCommentService.createFeedComment(
                CommentDto.FeedCommentDto.builder()
                        .userId(user.getId())
                        .commentText("카리나는 신이 맞아")
                        .build()
                , user.getId());

    }
    @Test
    @WithMockUser(username = "test1234@naver.com")
    public void createFeedComment_test() throws Exception{ //유저생성, 피드생성, 피드커맨트 생성
        User user = userRepository.findByEmail("test1234@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("유저가 없어용"));
        Feed feed = feedRepository.findByPostText("카리나는 신이야");

        CommentDto.FeedCommentRes feedCommentRes = feedCommentService.createFeedComment(
                CommentDto.FeedCommentDto.builder()
                        .userId(user.getId())
                        .commentText("고마츠 나나는 신이야")
                        .build()
                , user.getId());

        assertThat(feedCommentRes.getFeedId()).isEqualTo(feed.getId());
        assertThat(feedCommentRes.getUserId()).isEqualTo(user.getId());
        assertThat(feedCommentRes.getCommentText()).isEqualTo("고마츠 나나는 신이야");
        assertThat(feedCommentRes.getUsername()).isEqualTo("고마츠나나");
    }
    @Test
    public void getFeedComment_test(){
        Feed feed = feedRepository.findByPostText("카리나는 신이야");
        List<CommentDto.GetFeedCommentRes> feedComment = feedCommentService.getFeedComment(feed.getId());
        assertThat(feedComment.size()).isEqualTo(1);
        assertThat(feedComment.get(0).getCommentText()).isEqualTo("카리나는 신이 맞아");
        assertThat(feedComment.get(0).getUsername()).isEqualTo("고마츠나나");
    }

    @Test
    public void updateFeedComment_test(){
        User user = userRepository.findByEmail("test1234@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("유저가 없어용"));
        FeedComment feedComment = feedCommentRepository.findByUserId(user.getId());

        feedCommentService.updateFeedComment(feedComment.getId(),"나나");
        FeedComment updateFeedComment = feedCommentRepository.findByUserId(user.getId());

        assertThat(updateFeedComment.getCommentText()).isEqualTo("나나");
    }
    @Test
    public void deleteFeedComment_test(){
        User user = userRepository.findByEmail("test1234@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("유저가 없어용"));
        FeedComment feedComment = feedCommentRepository.findByUserId(user.getId());
        feedCommentService.deleteFeedComment(feedComment.getId());

        assertThrows(IllegalArgumentException.class, () -> {
            feedCommentRepository.findByIdAndState(feedComment.getId(), ACTIVE)
                    .orElseThrow(()-> new IllegalArgumentException("고마츠 나나는 왜이렇게 이쁠까"));
        });
    }


}
