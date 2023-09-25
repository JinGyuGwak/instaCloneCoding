package com.example.demo.src.comment.service;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.admin.repository.LogEntityRepository;
import com.example.demo.src.admin.entitiy.LogEntity;
import com.example.demo.src.feed.entitiy.Feed;
import com.example.demo.src.comment.entity.FeedComment;
import com.example.demo.src.comment.dto.response.FeedCommentRes;
import com.example.demo.src.comment.dto.response.GetFeedCommentRes;
import com.example.demo.src.comment.dto.response.UpdateFeedCommentRes;
import com.example.demo.src.func.FuncFeed;
import com.example.demo.src.func.FuncUser;
import com.example.demo.src.user.entitiy.User;
import com.example.demo.src.comment.repository.FeedCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.NOT_FIND_COMMENT;
import static com.example.demo.src.admin.entitiy.LogEntity.Domain.COMMENT;

@Transactional
@RequiredArgsConstructor
@Service
public class FeedCommentService {
    private final FuncUser funcUser;
    private final FuncFeed funcFeed;

    private final FeedCommentRepository feedCommentRepository;

    private final LogEntityRepository logEntityRepository;
    //피드 댓글 생성
    @Transactional
    public FeedCommentRes createFeedComment(Long feedId, Long userId, String comment){
        User user=funcUser.findUserByIdAndState(userId);
        Feed feed=funcFeed.findFeedByIdAndState(feedId);
        FeedComment feedComment = new FeedComment(feed,user, comment);
        feedCommentRepository.save(feedComment);

        LogEntity logEntity= new LogEntity(feed.getUser().getEmail(), COMMENT,"댓글생성");
        logEntityRepository.save(logEntity);
        return new FeedCommentRes(feedComment);
    }

    //피드 댓글 조회
    @Transactional
    public List<GetFeedCommentRes> searchFeedComment(Long feedId){
        return feedCommentRepository.findAllByFeedIdAndState(feedId, ACTIVE)
                .stream()
                .map(GetFeedCommentRes::new)
                .collect(Collectors.toList());
    }
    //피드 댓글 수정
    @Transactional
    public UpdateFeedCommentRes updateFeedComment(Long commentId, String updateComment){
        FeedComment feedComment=feedCommentRepository.findByIdAndState(commentId,ACTIVE).
                orElseThrow(()-> new BaseException(NOT_FIND_COMMENT));
        feedComment.updateFeedComment(updateComment);

        LogEntity logEntity= new LogEntity(feedComment.getUser().getEmail(), COMMENT,"댓글수정");
        logEntityRepository.save(logEntity);
        return new UpdateFeedCommentRes(commentId,updateComment);
    }

    //피드 댓글 삭제
    @Transactional
    public void deleteFeedComment(Long commentId){
        FeedComment feedComment = feedCommentRepository.findByIdAndState(commentId, ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_COMMENT));
        feedComment.deleteComment();
        LogEntity logEntity= new LogEntity(feedComment.getUser().getEmail(), COMMENT,"댓글삭제");
        logEntityRepository.save(logEntity);
    }
}
