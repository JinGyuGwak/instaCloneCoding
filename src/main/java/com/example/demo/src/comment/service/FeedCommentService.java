package com.example.demo.src.comment.service;

import com.example.demo.src.comment.dto.CommentDto.*;
import com.example.demo.src.feed.entitiy.Feed;
import com.example.demo.src.comment.entity.FeedComment;
import com.example.demo.src.util.FuncFeed;
import com.example.demo.src.util.FuncUser;
import com.example.demo.src.user.entitiy.User;
import com.example.demo.src.comment.repository.FeedCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.src.common.entity.BaseEntity.State.ACTIVE;

@RequiredArgsConstructor
@Service
public class FeedCommentService {
    private final FuncUser funcUser;
    private final FuncFeed funcFeed;

    private final FeedCommentRepository feedCommentRepository;

    //피드 댓글 생성
    @Transactional
    public FeedCommentRes createFeedComment(FeedCommentDto feedCommentDto, Long feedId){
        User user=funcUser.findUserByIdAndState(feedCommentDto.getUserId());
        Feed feed=funcFeed.findFeedByIdAndState(feedId);
        FeedComment feedComment = new FeedComment(feed,user, feedCommentDto.getCommentText());
        feedCommentRepository.save(feedComment);

        return new FeedCommentRes(feedComment);
    }

    //피드 댓글 조회
    @Transactional(readOnly = true)
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
                orElseThrow(()-> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        feedComment.updateFeedComment(updateComment);
        return UpdateFeedCommentRes.builder()
                .commentId(commentId)
                .commentText(updateComment)
                .build();
    }


    //피드 댓글 삭제
    @Transactional
    public void deleteFeedComment(Long commentId){
        FeedComment feedComment = feedCommentRepository.findByIdAndState(commentId, ACTIVE)
                .orElseThrow(()-> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        feedComment.deleteComment();
    }
}
