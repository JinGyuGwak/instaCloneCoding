package com.example.demo.src.feed;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.admin.LogEntityRepository;
import com.example.demo.src.admin.entity.LogEntity;
import com.example.demo.src.feed.entity.FeedComment;
import com.example.demo.src.feed.entity.ReComment;
import com.example.demo.src.feed.model.reComment.GetReCommentRes;
import com.example.demo.src.feed.model.reComment.ReCommentDto;
import com.example.demo.src.feed.model.reComment.ReCommentRes;
import com.example.demo.src.feed.model.reComment.UpdateReCommentRes;
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
import static com.example.demo.src.admin.entity.LogEntity.Domain.COMMENT;
@Transactional
@RequiredArgsConstructor
@Service
public class ReCommentService {

    private final UserRepository userRepository;
    private final FeedCommentRepository feedCommentRepository;

    private final ReCommentRepository reCommentRepository;

    private final LogEntityRepository logEntityRepository;
    //리댓글 생성
    public ReCommentRes createReComment(Long commentId, ReCommentDto reCommentDto){
        FeedComment feedComment=feedCommentRepository.findByIdAndState(commentId,ACTIVE)
                .orElseThrow(()->new BaseException(NOT_FIND_COMMENT));
        User user=userRepository.findByIdAndState(reCommentDto.getUserId(),ACTIVE)
                .orElseThrow(()->new BaseException(NOT_FIND_USER));

        ReComment reComment = new ReComment(feedComment ,user, reCommentDto.getReComment());
        reCommentRepository.save(reComment);

        LogEntity logEntity= new LogEntity(feedComment.getUser().getEmail(), COMMENT,"리댓글생성");
        logEntityRepository.save(logEntity);
        return new ReCommentRes(reComment);

    }
    //리댓글 조회
    public List<GetReCommentRes> searchReComment(Long commentId){
        List<ReComment> reCommentList =
                reCommentRepository.findAllByFeedCommentIdAndState(commentId,ACTIVE);

        List<GetReCommentRes> getReCommentResList = new ArrayList<>();
        for(ReComment reComment : reCommentList){
            GetReCommentRes a=new GetReCommentRes(reComment);
            getReCommentResList.add(a);
        }
        return getReCommentResList;
    }

    //리댓글 수정
    public UpdateReCommentRes updateReComment(Long reCommentId, String updateReCommentText){
        ReComment reComment=reCommentRepository.findByIdAndState(reCommentId,ACTIVE).
                orElseThrow(()-> new BaseException(NOT_FIND_COMMENT));
        reComment.updateReCommentText(updateReCommentText);

        LogEntity logEntity= new LogEntity(reComment.getUser().getEmail(), COMMENT,"리댓글수정");
        logEntityRepository.save(logEntity);

        return new UpdateReCommentRes(reCommentId,updateReCommentText);
    }

    //피드 리댓글 삭제
    public void deleteReComment(Long recommentId){
        ReComment reComment = reCommentRepository.findByIdAndState(recommentId, ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_COMMENT));
        reComment.deleteComment();
        LogEntity logEntity= new LogEntity(reComment.getUser().getEmail(), COMMENT,"리댓글삭제");
        logEntityRepository.save(logEntity);
    }
}
