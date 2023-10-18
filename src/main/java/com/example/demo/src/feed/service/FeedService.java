package com.example.demo.src.feed.service;

import com.example.demo.src.feed.dto.FeedDto;
import com.example.demo.src.feed.dto.FeedDto.*;
import com.example.demo.src.feed.entitiy.Feed;
import com.example.demo.src.feed.entitiy.FeedContent;
import com.example.demo.src.feed.entitiy.FeedReport;
import com.example.demo.src.common.config.S3Uploader;
import com.example.demo.src.user.service.UserService;
import com.example.demo.src.util.FuncFeed;
import com.example.demo.src.util.FuncUser;
import com.example.demo.src.user.entitiy.User;
import com.example.demo.src.feed.repository.FeedContentRepository;
import com.example.demo.src.feed.repository.FeedReportRepository;
import com.example.demo.src.feed.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.demo.src.common.entity.BaseEntity.State.ACTIVE;

@RequiredArgsConstructor
@Service
@Slf4j
public class FeedService {
    private final S3Uploader s3Uploader;
    private final FuncUser funcUser;
    private final FuncFeed funcFeed;
    private final FeedRepository feedRepository;
    private final FeedContentRepository feedContentRepository;
    private final FeedReportRepository feedReportRepository;
    private final UserService userService;

    @Transactional
    public PostFeedRes upload(Long userId, String postText , List<MultipartFile> files) throws Exception{
        if(files.size()>10){ //이미지가 10개가 넘어가면 안 됨
            throw new IllegalArgumentException("이미지는 10개까지 업로드 가능합니다.");
        }
        if(postText.length()>2200){ //글자수가 너무 크면 예외발생
            throw new IllegalArgumentException("본문 텍스트는 2200자 이하까지 입력 가능합니다.");
        }
        else if(postText.length()<1){
            throw new IllegalArgumentException("글자가 입력되지 않았습니다.");
        }

        Feed feed = Feed.builder()
                .user(userService.searchUserById(userId))
                .postText(postText)
                .build();
        if(Objects.requireNonNull(files.get(0).getOriginalFilename()).length()>2) {  //업로드 했는지 체크(업로드 안했으면 length 는 0임)
            List<FeedContent> contentList = s3Uploader.uploadFiles(files, "static");
            if (!contentList.isEmpty()) {
                for (FeedContent feedContent : contentList) {
                    FeedContent oneFeed = feedContentRepository.save(feedContent);
                    feed.addFeedContent(oneFeed); //시간이 좀 흘러야 이게 되는구나 뭐지 ?
                }
            }
        }

        Feed saveFeed = feedRepository.save(feed);
        return new PostFeedRes(saveFeed);
    }

    //피드 텍스트 변경(업데이트)
    @Transactional
    public UpdateFeedDto updateFeed(UpdateFeedDto feedDto){
        String postText = feedDto.getUpdateText();
        Long feedId = feedDto.getFeedId();
        if(postText.length()>2200){ //글자수가 너무 크면 예외발생
            throw new IllegalArgumentException("2200자 까지 입력 가능합니다.");
        }
        else if(postText.length()<1){
            throw new IllegalArgumentException("글자가 입력되지 않았습니다.");
        }
        Feed feed=funcFeed.findFeedByIdAndState(feedId);
        feed.update(postText);
        return UpdateFeedDto.builder()
                .feedId(feedId)
                .updateText(postText)
                .build();
    }
    //피드 전체조회
    @Transactional(readOnly = true)
    public List<GetFeedRes> searchAllFeed(Pageable pageable){
        return feedRepository.findAllByState(ACTIVE, pageable)
                .stream()
                .filter(feed -> feed.getReportCount() < 6)
                .map(GetFeedRes::new)
                .collect(Collectors.toList());
    }
    //유저의 피드 조회
    @Transactional(readOnly = true)
    public List<GetFeedRes> searchUserFeed(Long userId){
        return feedRepository.findAllByUserIdAndState(userId, ACTIVE)
                .stream()
                .filter(feed -> feed.getReportCount() < 6)
                .map(GetFeedRes::new)
                .collect(Collectors.toList());
    }
    //삭제 구현하기
    @Transactional
    public void deleteFeed(Long feedId){
        Feed feed = funcFeed.findFeedByIdAndState(feedId);
        feed.deleteFeed();
    }
    //피드 신고
    @Transactional
    public FeedReportDto feedReport(FeedReportDto feedReportDto){
        Feed feed=funcFeed.findFeedByIdAndState(feedReportDto.getFeedId());
        User user=funcUser.findUserByIdAndState(feedReportDto.getUserId());
        FeedReport feedReport = new FeedReport(feed,user, feedReportDto.getReason());
        feedReportRepository.save(feedReport);
        feed.reported();
        return new FeedReportDto(feedReport);
    }
}
