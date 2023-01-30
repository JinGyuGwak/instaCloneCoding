package com.example.demo.src.feed;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.admin.LogEntityRepository;
import com.example.demo.src.admin.entity.LogEntity;
import com.example.demo.src.feed.entity.Feed;
import com.example.demo.src.feed.entity.FeedComment;
import com.example.demo.src.feed.model.feedComment.FeedCommentDto;
import com.example.demo.src.feed.model.feedComment.FeedCommentRes;
import com.example.demo.src.feed.model.feedComment.GetFeedCommentRes;
import com.example.demo.src.feed.model.feedComment.UpdateFeedCommentRes;
import com.example.demo.src.feed.repository.*;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.*;
import static com.example.demo.common.response.BaseResponseStatus.NOT_FIND_COMMENT;
import static com.example.demo.src.admin.entity.LogEntity.Domain.COMMENT;

@Transactional
@RequiredArgsConstructor
@Service
public class FeedCommentService {
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final FeedCommentRepository feedCommentRepository;

    private final LogEntityRepository logEntityRepository;
    //피드 댓글 생성
    public FeedCommentRes createFeedComment(Long feedId, FeedCommentDto feedCommentDto){
        User user=userRepository.findByIdAndState(feedCommentDto.getUserId(),ACTIVE)
                .orElseThrow(()->new BaseException(NOT_FIND_USER));
        Feed feed=feedRepository.findByIdAndState(feedId,ACTIVE)
                .orElseThrow(()->new BaseException(NOT_FIND_FEED));
        FeedComment feedComment = new FeedComment(feed,user, feedCommentDto.getFeedComment());
        feedCommentRepository.save(feedComment);

        LogEntity logEntity= new LogEntity(feed.getUser().getEmail(), COMMENT,"댓글생성");
        logEntityRepository.save(logEntity);
        return new FeedCommentRes(feedComment);
    }


    //피드 댓글 조회
    public List<GetFeedCommentRes> searchFeedComment(Long feedId){
        List<FeedComment> feedCommentList =
                feedCommentRepository.findAllByFeedIdAndState(feedId,ACTIVE);
        List<GetFeedCommentRes> getFeedCommentRes = new ArrayList<>();
        for(FeedComment feedComment : feedCommentList){
            GetFeedCommentRes a=new GetFeedCommentRes(feedComment);
            getFeedCommentRes.add(a);
        }
        return getFeedCommentRes;
    }
    //피드 댓글 수정
    public UpdateFeedCommentRes updateFeedComment(Long commentId, String updateComment){
        FeedComment feedComment=feedCommentRepository.findByIdAndState(commentId,ACTIVE).
                orElseThrow(()-> new BaseException(NOT_FIND_COMMENT));
        feedComment.updateFeedComment(updateComment);

        LogEntity logEntity= new LogEntity(feedComment.getUser().getEmail(), COMMENT,"댓글수정");
        logEntityRepository.save(logEntity);
        return new UpdateFeedCommentRes(commentId,updateComment);
    }

    //피드 댓글 삭제
    public void deleteFeedComment(Long commentId){
        FeedComment feedComment = feedCommentRepository.findByIdAndState(commentId, ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_COMMENT));
        feedComment.deleteComment();
        LogEntity logEntity= new LogEntity(feedComment.getUser().getEmail(), COMMENT,"댓글삭제");
        logEntityRepository.save(logEntity);
    }
}
