package com.example.demo.src.feed.repository;

import com.example.demo.src.feed.entitiy.FeedLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedLikeRepository extends JpaRepository<FeedLike,Long> {
    Optional<FeedLike> findByFeedIdAndUserId(Long feedId, Long userId);
    List<FeedLike> findByFeedId(Long feedId);

}
