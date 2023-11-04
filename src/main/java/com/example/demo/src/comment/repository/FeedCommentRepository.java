package com.example.demo.src.comment.repository;

import com.example.demo.src.comment.entity.FeedComment;
import com.example.demo.src.common.entity.BaseEntity.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {

    FeedComment findByUserId(Long userId); //테스트용

    List<FeedComment> findAllByFeedIdAndState(Long feedId, State state);
    Optional<FeedComment> findByIdAndState(Long id, State state);

}
