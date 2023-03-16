package com.example.demo.src.feed.service;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.admin.LogEntityRepository;
import com.example.demo.src.admin.entity.LogEntity;
import com.example.demo.src.feed.S3Uploader;
import com.example.demo.src.feed.entity.*;
import com.example.demo.src.feed.model.feed.*;
import com.example.demo.src.feed.model.feedReport.FeedReportDto;
import com.example.demo.src.feed.model.feedReport.FeedReportRes;
import com.example.demo.src.feed.repository.*;
import com.example.demo.src.func.FuncFeed;
import com.example.demo.src.func.FuncUser;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.*;
import static com.example.demo.src.admin.entity.LogEntity.Domain.*;

@Transactional
@RequiredArgsConstructor
@Service
public class FeedService {
    private final S3Uploader s3Uploader;
    private final FuncUser funcUser;
    private final FuncFeed funcFeed;
    private final FeedRepository feedRepository;
    private final FeedContentRepository feedContentRepository;
    private final FeedReportRepository feedReportRepository;
    private final LogEntityRepository logEntityRepository;

    public PostFeedRes upload(FeedCreateRequestDto requestDto , List<MultipartFile> files) throws Exception{
        if(files.size()>10){ //이미지가 10개가 넘어가면 안 됨
            throw new BaseException(OVER_IMAGE);
        }

        if(requestDto.getPostText().length()>2200){ //글자수가 너무 크면 예외발생
            throw new BaseException(OVER_COMMENT);
        }
        else if(requestDto.getPostText().length()<1){
            throw new BaseException(LEAST_COMMENT);
        }

        Feed feed = new Feed(
                requestDto.getUser(),
                requestDto.getPostText()
        );

        List<FeedContent> contentList = s3Uploader.uploadFiles(files,"static");

        if(!contentList.isEmpty()){
            for (FeedContent feedContent : contentList){
                System.out.println("feedContent.getPostURL() = " + feedContent.getPostURL());
                System.out.println("feedContent.getPostName() = " + feedContent.getPostName());
                feed.addFeedContent(feedContentRepository.save(feedContent));
            }
        } else{
            throw new BaseException(LEAST_IMAGE); //최소 1개의 이미지를 올려야함
        }

        Feed saveFeed = feedRepository.save(feed);

        LogEntity logEntity= new LogEntity(feed.getUser().getEmail(),FEED,"피드생성");
        logEntityRepository.save(logEntity);

        return new PostFeedRes(saveFeed);
    }

    //피드 텍스트 변경(업데이트)
    @Transactional
    public UpdateFeedRes updateFeed(Long id, FeedUpdateRequestDto requestDto){

        if(requestDto.getPostText().length()>2200){ //글자수가 너무 크면 예외발생
            throw new BaseException(OVER_COMMENT);
        }
        else if(requestDto.getPostText().length()<1){
            throw new BaseException(LEAST_COMMENT);
        }
        Feed feed=funcFeed.findFeedByIdAndState(id);
        feed.update(requestDto.getPostText());

        LogEntity logEntity= new LogEntity(feed.getUser().getEmail(),FEED,"피드수정");
        logEntityRepository.save(logEntity);
        return new UpdateFeedRes(id);
    }

    //유저의 피드 조회
    @Transactional(readOnly = true)
    public List<GetFeedRes> searchUserFeed(Long userId) throws BaseException{
        try{
            List<Feed> feedList = feedRepository.findAllByUserIdAndState(userId,ACTIVE);
            List<GetFeedRes> getFeedRes = new ArrayList<>();
            for(Feed a : feedList){
                GetFeedRes b = new GetFeedRes(a);
                getFeedRes.add(b);
            }
            return getFeedRes;
        }
        catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //피드 전체조회
    @Transactional(readOnly = true)
    public List<GetFeedRes> searchAllFeed(Pageable pageable) throws BaseException{
        try{
            return feedRepository.findAllByState(ACTIVE, pageable)
                    .stream()
                    .map(GetFeedRes::new)
                    .collect(Collectors.toList());
        }
        catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //삭제 구현하기
    public void deleteFeed(Long feedId){
        Feed feed = funcFeed.findFeedByIdAndState(feedId);
        feed.deleteFeed();

        LogEntity logEntity= new LogEntity(feed.getUser().getEmail(), FEED,"피드삭제");
        logEntityRepository.save(logEntity);
    }

    //피드 신고
    public FeedReportRes feedReport(FeedReportDto feedReportDto){
        Feed feed=funcFeed.findFeedByIdAndState(feedReportDto.getFeedId());
        User user=funcUser.findUserByIdAndState(feedReportDto.getUserId());

        FeedReport feedReport = new FeedReport(feed,user,feedReportDto.getReason());
        feedReportRepository.save(feedReport);

        LogEntity logEntity= new LogEntity(feedReport.getUser().getEmail(), REPORT,"피드신고");
        logEntityRepository.save(logEntity);
        return new FeedReportRes(feedReport);
    }

}
