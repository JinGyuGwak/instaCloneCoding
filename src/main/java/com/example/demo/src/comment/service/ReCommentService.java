package com.example.demo.src.comment.service;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.admin.repository.LogEntityRepository;
import com.example.demo.src.admin.entitiy.LogEntity;
import com.example.demo.src.comment.entity.FeedComment;
import com.example.demo.src.comment.entity.ReComment;
import com.example.demo.src.comment.dto.response.GetReCommentRes;
import com.example.demo.src.comment.dto.response.ReCommentRes;
import com.example.demo.src.comment.dto.response.UpdateReCommentRes;
import com.example.demo.src.func.FuncUser;
import com.example.demo.src.user.entitiy.User;
import com.example.demo.src.comment.repository.FeedCommentRepository;
import com.example.demo.src.comment.repository.ReCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.NOT_FIND_COMMENT;
import static com.example.demo.src.admin.entitiy.LogEntity.Domain.COMMENT;
@RequiredArgsConstructor
@Service
public class ReCommentService {

    private final FuncUser funcUser;

    private final FeedCommentRepository feedCommentRepository;

    private final ReCommentRepository reCommentRepository;

    private final LogEntityRepository logEntityRepository;

    //리댓글 생성
    @Transactional
    public ReCommentRes createReComment(Long commentId, Long userId, String comment){
        FeedComment feedComment=feedCommentRepository.findByIdAndState(commentId,ACTIVE)
                .orElseThrow(()->new BaseException(NOT_FIND_COMMENT));
        User user=funcUser.findUserByIdAndState(userId);

        ReComment reComment = new ReComment(feedComment ,user, comment);
        reCommentRepository.save(reComment);

        LogEntity logEntity= new LogEntity(feedComment.getUser().getEmail(), COMMENT,"리댓글생성");
        logEntityRepository.save(logEntity);
        return new ReCommentRes(reComment);

    }
    //리댓글 조회
    @Transactional
    public List<GetReCommentRes> searchReComment(Long commentId){
        return reCommentRepository.findAllByFeedCommentIdAndState(commentId, ACTIVE).stream()
                .map(GetReCommentRes::new)
                .collect(Collectors.toList());
    }

    //리댓글 수정
    @Transactional
    public UpdateReCommentRes updateReComment(Long reCommentId, String updateReCommentText){
        ReComment reComment=findReCommentByIdAndState(reCommentId);
        reComment.updateReCommentText(updateReCommentText);

        LogEntity logEntity= new LogEntity(reComment.getUser().getEmail(), COMMENT,"리댓글수정");
        logEntityRepository.save(logEntity);

        return new UpdateReCommentRes(reCommentId,updateReCommentText);
    }

    //피드 리댓글 삭제
    @Transactional
    public void deleteReComment(Long recommentId){
        ReComment reComment = findReCommentByIdAndState(recommentId);
        reComment.deleteComment();
        LogEntity logEntity= new LogEntity(reComment.getUser().getEmail(), COMMENT,"리댓글삭제");
        logEntityRepository.save(logEntity);
    }

    //ReComment 조회
    private ReComment findReCommentByIdAndState(Long id){
        return reCommentRepository.findByIdAndState(id,ACTIVE).
                orElseThrow(()-> new BaseException(NOT_FIND_COMMENT));
    }

}
