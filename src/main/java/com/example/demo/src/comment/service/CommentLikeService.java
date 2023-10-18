package com.example.demo.src.comment.service;

import com.example.demo.src.comment.dto.CommentLikeDto;
import com.example.demo.src.comment.entity.CommentLike;
import com.example.demo.src.comment.entity.FeedComment;
import com.example.demo.src.util.FuncUser;
import com.example.demo.src.user.entitiy.User;
import com.example.demo.src.comment.repository.CommentLikeRepository;
import com.example.demo.src.comment.repository.FeedCommentRepository;
import com.example.demo.src.util.LoginUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.src.common.entity.BaseEntity.State.ACTIVE;

@RequiredArgsConstructor
@Service
public class CommentLikeService {

    private final FeedCommentRepository feedCommentRepository;
    private final FuncUser funcUser;
    private final CommentLikeRepository commentLikeRepository;

    //댓글 좋아요
    @Transactional
    public CommentLikeDto commentLike(Long commentId){
        User user=funcUser.findUserByEmail(LoginUtil.getLoginEmail());
        FeedComment feedComment = feedCommentRepository.findByIdAndState(commentId, ACTIVE)
                .orElseThrow(()-> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        CommentLike commentLike = new CommentLike(user,feedComment);
        if(!commentLikeRepository.findByUserIdAndFeedCommentId(user.getId(), feedComment.getId()).isPresent()){
            commentLikeRepository.save(commentLike); //CommentLike 가 존재하지 않으면 생성(중복방지)
        }
        return new CommentLikeDto(commentLike);
    }
    //댓글 좋아요 조회
    @Transactional(readOnly = true)
    public List<CommentLikeDto> commentLikeSearch(Long commentId){
        return commentLikeRepository.findByFeedCommentIdAndState(commentId, ACTIVE)
                .stream()
                .map(CommentLikeDto::new)
                .collect(Collectors.toList());
    }
    //댓글 좋아요 삭제
    @Transactional
    public void commentLikeDelete(Long commentId){
        User loginUser = funcUser.findUserByEmail(LoginUtil.getLoginEmail());
        CommentLike commentLike=commentLikeRepository.findByUserIdAndFeedCommentId(loginUser.getId(),commentId)
                .orElseThrow(()-> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        commentLikeRepository.delete(commentLike);
    }
}
