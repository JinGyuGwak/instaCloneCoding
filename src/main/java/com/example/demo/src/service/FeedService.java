package com.example.demo.src.service;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.repository.LogEntityRepository;
import com.example.demo.src.entity.LogEntity;
import com.example.demo.src.entity.Feed;
import com.example.demo.src.entity.FeedContent;
import com.example.demo.src.entity.FeedReport;
import com.example.demo.common.config.S3Uploader;
import com.example.demo.src.request.FeedCreateRequestDto;
import com.example.demo.src.response.FeedReportRes;
import com.example.demo.src.func.FuncFeed;
import com.example.demo.src.func.FuncUser;
import com.example.demo.src.entity.User;
import com.example.demo.src.repository.FeedContentRepository;
import com.example.demo.src.repository.FeedReportRepository;
import com.example.demo.src.repository.FeedRepository;
import com.example.demo.src.response.GetFeedRes;
import com.example.demo.src.response.PostFeedRes;
import com.example.demo.src.response.UpdateFeedRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.*;
import static com.example.demo.src.entity.LogEntity.Domain.*;

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

    @Transactional
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
    public UpdateFeedRes updateFeed(Long id, String postText){

        if(postText.length()>2200){ //글자수가 너무 크면 예외발생
            throw new BaseException(OVER_COMMENT);
        }
        else if(postText.length()<1){
            throw new BaseException(LEAST_COMMENT);
        }
        Feed feed=funcFeed.findFeedByIdAndState(id);
        feed.update(postText);

        LogEntity logEntity= new LogEntity(feed.getUser().getEmail(),FEED,"피드수정");
        logEntityRepository.save(logEntity);
        return new UpdateFeedRes(id);
    }

    //유저의 피드 조회
    @Transactional(readOnly = true)
    public List<GetFeedRes> searchUserFeed(Long userId) throws BaseException{
        try{
            return feedRepository.findAllByUserIdAndState(userId, ACTIVE)
                    .stream()
                    .filter(feed -> feed.getReportCount() < 6)
                    .map(GetFeedRes::new)
                    .collect(Collectors.toList());
        }
        catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //피드 전체조회
    @Transactional(readOnly = true)
    public List<GetFeedRes> searchAllFeed(Pageable pageable) throws BaseException{
        try{
            List<GetFeedRes> result = new ArrayList<>();
            List<Feed> feeds = feedRepository.findAllByState(ACTIVE,pageable);

            for (Feed feed : feeds) {
                System.out.println("지연로딩 쿼리 테스트 feed.getFeedReportList().size() =" + feed.getFeedReportList().size());
                if(feed.getFeedReportList().size()<6){
                    result.add(new GetFeedRes(feed));
                }
            }
            return result;

//            return feedRepository.findAllByState(ACTIVE, pageable)
//                    .stream()
//                    .filter(feed -> feed.getReportCount() < 6)
//                    .map(GetFeedRes::new)
//                    .collect(Collectors.toList());


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
    public FeedReportRes feedReport(Long userId, Long feedId, String reason){
        Feed feed=funcFeed.findFeedByIdAndState(userId);
        User user=funcUser.findUserByIdAndState(feedId);

        FeedReport feedReport = new FeedReport(feed,user,reason);
        feedReportRepository.save(feedReport);
        feed.reported();

        LogEntity logEntity= new LogEntity(feedReport.getUser().getEmail(), REPORT,"피드신고");
        logEntityRepository.save(logEntity);
        return new FeedReportRes(feedReport);
    }

}
