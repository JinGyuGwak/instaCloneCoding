package com.example.demo.comment.service;

import com.example.demo.src.comment.dto.CommentDto;
import com.example.demo.src.comment.dto.ReCommentDto;
import com.example.demo.src.comment.entity.FeedComment;
import com.example.demo.src.comment.entity.ReComment;
import com.example.demo.src.comment.repository.CommentLikeRepository;
import com.example.demo.src.comment.repository.FeedCommentRepository;
import com.example.demo.src.comment.repository.ReCommentRepository;
import com.example.demo.src.comment.service.CommentLikeService;
import com.example.demo.src.comment.service.FeedCommentService;
import com.example.demo.src.comment.service.ReCommentService;
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

import static com.example.demo.src.common.entity.BaseEntity.State.ACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ReCommentServiceTest {
    @Autowired
    private FeedCommentRepository feedCommentRepository;
    @Autowired
    private ReCommentService reCommentService;
    @Autowired
    private ReCommentRepository reCommentRepository;
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

        CommentDto.FeedCommentRes feedComment = feedCommentService.createFeedComment(
                CommentDto.FeedCommentDto.builder()
                        .userId(user.getId())
                        .commentText("카리나는 신이 맞아")
                        .build()
                , user.getId());

        reCommentService.createReComment(feedComment.getId(),"고마츠 나나!");
    }
    @Test
    @WithMockUser(username = "test2695@naver.com")
    public void createReComment_test() {
        User user = userRepository.findByEmail("test2695@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("넥스트레벨"));
        FeedComment feedComment = feedCommentRepository.findByUserId(user.getId());

        reCommentService.createReComment(feedComment.getId(),"설윤도 이쁜 것 같아");
        ReComment reComment = reCommentRepository.findByReCommentText("설윤도 이쁜 것 같아");

        assertThat(reComment.getReCommentText()).isEqualTo("설윤도 이쁜 것 같아");
        assertThat(reComment.getUser().getEmail()).isEqualTo("test2695@naver.com");
    }
    @Test
    @WithMockUser(username = "test2695@naver.com")
    public void searchReComment(){
        User user = userRepository.findByEmail("test2695@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("넥스트레벨"));
        FeedComment feedComment = feedCommentRepository.findByUserId(user.getId());

        List<ReCommentDto.GetReCommentRes> reCommentRes =
                reCommentService.searchReComment(feedComment.getId());
        assertThat(reCommentRes.size()).isEqualTo(1);
        assertThat(reCommentRes.get(0).getReComment()).isEqualTo("고마츠 나나!");
    }
    @Test
    @WithMockUser(username = "test2695@naver.com")
    public void updateReComment(){
        User user = userRepository.findByEmail("test2695@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("넥스트레벨"));
        ReComment reComment = reCommentRepository.findByReCommentText("고마츠 나나!");

        reCommentService.updateReComment(reComment.getId(),"이제 곧 끝");
        ReComment updateReComment = reCommentRepository.findByReCommentText("이제 곧 끝");

        assertThat(updateReComment.getReCommentText()).isEqualTo("이제 곧 끝");
        assertThat(updateReComment.getFeedComment()).isEqualTo(reComment.getFeedComment());
    }
    @Test
    @WithMockUser(username = "test2695@naver.com")
    public void deleteReComment(){
        ReComment reComment = reCommentRepository.findByReCommentText("고마츠 나나!");

        reCommentService.deleteReComment(reComment.getId());
        assertThrows(IllegalArgumentException.class, () -> {
            reCommentRepository.findByIdAndState(reComment.getId(),ACTIVE)
                    .orElseThrow(() -> new IllegalArgumentException("테스트 코드 끝"));
        });
    }
}
