package com.example.demo.src.comment.repository;

import com.example.demo.src.comment.entity.ReComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import static com.example.demo.src.common.entity.BaseEntity.*;

public interface ReCommentRepository extends JpaRepository<ReComment,Long> {
    List<ReComment> findAllByFeedCommentIdAndState(Long commentId, State state);
    Optional<ReComment> findByIdAndState(Long reCommentId, State state);
}
