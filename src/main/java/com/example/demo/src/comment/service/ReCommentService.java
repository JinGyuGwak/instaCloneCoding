package com.example.demo.src.comment.service;

import com.example.demo.src.comment.dto.ReCommentDto;
import com.example.demo.src.comment.dto.ReCommentDto.GetReCommentRes;
import com.example.demo.src.comment.dto.ReCommentDto.ReCommentRes;
import com.example.demo.src.comment.dto.ReCommentDto.UpdateReCommentRes;
import com.example.demo.src.comment.entity.FeedComment;
import com.example.demo.src.comment.entity.ReComment;
import com.example.demo.src.util.FuncUser;
import com.example.demo.src.user.entitiy.User;
import com.example.demo.src.comment.repository.FeedCommentRepository;
import com.example.demo.src.comment.repository.ReCommentRepository;
import com.example.demo.src.util.LoginUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.src.common.entity.BaseEntity.State.ACTIVE;
@RequiredArgsConstructor
@Service
public class ReCommentService {

    private final FuncUser funcUser;
    private final FeedCommentRepository feedCommentRepository;
    private final ReCommentRepository reCommentRepository;


    //리댓글 생성
    @Transactional
    public ReCommentRes createReComment(Long commentId, String comment){
        FeedComment feedComment=feedCommentRepository.findByIdAndState(commentId,ACTIVE)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 댓글입니다."));
        User user=funcUser.findUserByEmail(LoginUtil.getLoginEmail());

        ReComment reComment = new ReComment(feedComment ,user, comment);
        reCommentRepository.save(reComment);

        return new ReCommentRes(reComment);

    }
    //리댓글 조회
    @Transactional(readOnly = true)
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
        return UpdateReCommentRes.builder()
                .reCommentId(reCommentId)
                .reComment(updateReCommentText)
                .build();
    }

    //피드 리댓글 삭제
    @Transactional
    public void deleteReComment(Long recommentId){
        ReComment reComment = findReCommentByIdAndState(recommentId);
        reComment.deleteComment();
    }

    //ReComment 조회
    @Transactional(readOnly = true)
    public ReComment findReCommentByIdAndState(Long id){
        return reCommentRepository.findByIdAndState(id,ACTIVE).
                orElseThrow(()-> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
    }

}
