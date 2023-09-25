package com.example.demo.src.comment.repository;

import com.example.demo.src.comment.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import static com.example.demo.common.entity.BaseEntity.*;

public interface CommentLikeRepository extends JpaRepository<CommentLike,Long> {
    Optional<CommentLike> findByUserIdAndFeedCommentId(Long userId, Long feedCommentId);
    List<CommentLike> findByFeedCommentIdAndState(Long feedCommentId, State state);
}
