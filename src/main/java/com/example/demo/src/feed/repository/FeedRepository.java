package com.example.demo.src.feed.repository;

import com.example.demo.common.entity.BaseEntity.*;
import com.example.demo.src.feed.entity.Feed;
import com.example.demo.src.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {


    Optional<Feed> findByIdAndState(Long id, State state);

    @EntityGraph(attributePaths = "feedContentList")
    @Query("select f from Feed f")
    List<Feed> findAllByState(State state, Pageable pageable);
    List<Feed> findAllByUserIdAndState(Long userId, State state);

    Page<Feed> findByState(State state, Pageable pageable);
    Page<Feed> findAllByCreatedAt(LocalDateTime created, Pageable pageable);
    Page<Feed> findByUserId(Long userId, Pageable pageable);


}
