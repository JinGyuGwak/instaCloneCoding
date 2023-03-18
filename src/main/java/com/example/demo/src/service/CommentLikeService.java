package com.example.demo.src.service;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.entity.CommentLike;
import com.example.demo.src.entity.FeedComment;
import com.example.demo.src.response.CommentLikeRes;
import com.example.demo.src.response.GetCommentLike;
import com.example.demo.src.func.FuncUser;
import com.example.demo.src.entity.User;
import com.example.demo.src.repository.CommentLikeRepository;
import com.example.demo.src.repository.FeedCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.NOT_FIND_COMMENT;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentLikeService {

    private final FeedCommentRepository feedCommentRepository;
    private final FuncUser funcUser;
    private final CommentLikeRepository commentLikeRepository;

    //댓글 좋아요
    @Transactional
    public CommentLikeRes commentLike(Long userId, Long commentId){
        User user=funcUser.findUserByIdAndState(userId);
        FeedComment feedComment = feedCommentRepository.findByIdAndState(
                        commentId, ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_COMMENT));
        CommentLike commentLike = new CommentLike(user,feedComment);
        if(!commentLikeRepository.findByUserIdAndFeedCommentId(user.getId(), feedComment.getId()).isPresent()){
            commentLikeRepository.save(commentLike);
        }
        return new CommentLikeRes(commentLike);
    }
    //댓글 좋아요 조회
    @Transactional
    public List<GetCommentLike> commentLikeSearch(Long commentId){
        return commentLikeRepository.findByFeedCommentIdAndState(commentId, ACTIVE)
                .stream()
                .map(GetCommentLike::new)
                .collect(Collectors.toList());
    }
    //댓글 좋아요 삭제
    @Transactional
    public void commentLikeDelete(Long commentId,Long userId){
        CommentLike commentLike=commentLikeRepository.findByUserIdAndFeedCommentId(userId,commentId)
                .orElseThrow(()-> new BaseException(NOT_FIND_COMMENT));
        commentLikeRepository.delete(commentLike);
    }
}
