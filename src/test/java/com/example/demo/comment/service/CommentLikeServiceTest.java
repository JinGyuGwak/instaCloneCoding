package com.example.demo.comment.service;


import com.example.demo.src.comment.dto.CommentDto;
import com.example.demo.src.comment.dto.CommentLikeDto;
import com.example.demo.src.comment.entity.FeedComment;
import com.example.demo.src.comment.repository.CommentLikeRepository;
import com.example.demo.src.comment.repository.FeedCommentRepository;
import com.example.demo.src.comment.service.CommentLikeService;
import com.example.demo.src.comment.service.FeedCommentService;
import com.example.demo.src.feed.dto.FeedDto;
import com.example.demo.src.feed.entitiy.FeedContent;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CommentLikeServiceTest {
    @Autowired
    private FeedCommentRepository feedCommentRepository;
    @Autowired
    private CommentLikeRepository commentLikeRepository;
    @Autowired
    private CommentLikeService commentLikeService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FeedService feedService;
    @Autowired
    private FeedCommentService feedCommentService;
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
    User user(){
        return User.builder()
                .email("test2695@naver.com")
                .name("카리나")
                .password("269599zz")
                .build();
    }
    @BeforeEach
    @WithMockUser(username = "test2695@naver.com")
    public void be() throws Exception{ //테스트 시작 전 유저등록, 피드등록, 피드커멘트 등록

        List<FeedContent> list = new ArrayList<>();
        list.add(dummyFeedContent1());
        given(s3Uploader.uploadFiles(any(),anyString())).willReturn(list);

        List<MultipartFile> files = new ArrayList<>();
        files.add(getMockMultipart1());

        User user = userRepository.save(user());
        given(userService.searchUserById(anyLong())).willReturn(user);

        feedService.upload(user.getId(), files, "카리나는 신이야");

        feedCommentService.createFeedComment(
                CommentDto.FeedCommentDto.builder()
                        .userId(user.getId())
                        .commentText("카리나는 신이 맞아")
                        .build()
                ,user.getId());

    }
    @Test
    @WithMockUser(username = "test2695@naver.com")
    public void commentLike_test(){
        User user = userRepository.findByEmail("test2695@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("넥스트레벨"));

        FeedComment feedComment = feedCommentRepository.findByUserId(user.getId());
        CommentLikeDto commentLikeDto = commentLikeService.commentLike(feedComment.getId());

        assertThat(commentLikeDto.getCommentId()).isEqualTo(feedComment.getId());
        assertThat(commentLikeDto.getUserId()).isEqualTo(user.getId());
    }
    @Test
    @WithMockUser(username = "test2695@naver.com")
    public void commentLikeSearch_test(){ //commentLike 생성
        User user = userRepository.findByEmail("test2695@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("넥스트레벨"));

        FeedComment feedComment = feedCommentRepository.findByUserId(user.getId());

        commentLikeService.commentLike(feedComment.getId());
        List<CommentLikeDto> commentLikeList = commentLikeService.commentLikeSearch(feedComment.getId());

        assertThat(commentLikeList.get(0).getUserId()).isEqualTo(user.getId());
        assertThat(commentLikeList.get(0).getCommentId()).isEqualTo(feedComment.getId());
    }
    @Test
    @WithMockUser(username = "test2695@naver.com")
    public void commentLikeDelete_test(){
        User user = userRepository.findByEmail("test2695@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("넥스트레벨"));

        FeedComment feedComment = feedCommentRepository.findByUserId(user.getId());

        commentLikeService.commentLike(feedComment.getId());
        commentLikeService.commentLikeDelete(feedComment.getId());

        assertThrows(IllegalArgumentException.class, () -> {
            commentLikeRepository
                    .findByUserIdAndFeedCommentId(user.getId(), feedComment.getId())
                    .orElseThrow(()->new IllegalArgumentException("카리나는 진짜 왜이렇게 이쁜걸까"));
        });
    }
    @Test
    @WithMockUser(username = "test1234@naver.com")
    public void createFeedComment_test() throws Exception{ //유저생성, 피드생성, 피드커맨트 생성
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

        FeedDto.PostFeedRes feed = feedService.upload(user.getId(), files, "카리나는 신이야");

        CommentDto.FeedCommentRes feedCommentRes = feedCommentService.createFeedComment(
                CommentDto.FeedCommentDto.builder()
                        .userId(user.getId())
                        .commentText("카리나는 신이 맞아")
                        .build()
                , user.getId());

        assertThat(feedCommentRes.getFeedId()).isEqualTo(feed.getFeedId());
        assertThat(feedCommentRes.getUserId()).isEqualTo(user1.getId());
        assertThat(feedCommentRes.getCommentText()).isEqualTo("카리나는 신이 맞아");
        assertThat(feedCommentRes.getUsername()).isEqualTo("고마츠나나");
    }



}
