package com.example.demo.src.feed;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.admin.LogEntityRepository;
import com.example.demo.src.feed.entity.CommentLike;
import com.example.demo.src.feed.entity.FeedComment;
import com.example.demo.src.feed.model.commentLike.CommentLikeRes;
import com.example.demo.src.feed.model.commentLike.GetCommentLike;
import com.example.demo.src.feed.model.commentLike.PostCommentLikeDto;
import com.example.demo.src.feed.repository.*;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.NOT_FIND_COMMENT;
import static com.example.demo.common.response.BaseResponseStatus.NOT_FIND_USER;
@Transactional
@RequiredArgsConstructor
@Service
public class CommentLikeService {

    private final UserRepository userRepository;
    private final FeedCommentRepository feedCommentRepository;

    private final CommentLikeRepository commentLikeRepository;

    //댓글 좋아요
    public CommentLikeRes commentLike(PostCommentLikeDto postCommentLikeDto){
        User user=userRepository.findByIdAndState(postCommentLikeDto.getUserId(),ACTIVE)
                .orElseThrow(()->new BaseException(NOT_FIND_USER));
        FeedComment feedComment = feedCommentRepository.findByIdAndState(
                        postCommentLikeDto.getCommentId(), ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_COMMENT));

        CommentLike commentLike = new CommentLike(user,feedComment);
        if(!commentLikeRepository.findByUserIdAndFeedCommentId(user.getId(), feedComment.getId()).isPresent()){
            commentLikeRepository.save(commentLike);
        }
        return new CommentLikeRes(commentLike);
    }
    //댓글 좋아요 조회
    public List<GetCommentLike> commentLikeSearch(Long commentId){
        List<CommentLike> commentLikeList =
                commentLikeRepository.findByFeedCommentIdAndState(commentId,ACTIVE);
        List<GetCommentLike> getCommentLikeList = new ArrayList<>();
        for(CommentLike commentLike : commentLikeList){
            GetCommentLike a = new GetCommentLike(commentLike);
            getCommentLikeList.add(a);
        }
        return getCommentLikeList;
    }
    //댓글 좋아요 삭제
    public void commentLikeDelete(Long commentId,Long userId){
        CommentLike commentLike=commentLikeRepository.findByUserIdAndFeedCommentId(userId,commentId)
                .orElseThrow(()-> new BaseException(NOT_FIND_COMMENT));
        commentLikeRepository.delete(commentLike);
    }
}
