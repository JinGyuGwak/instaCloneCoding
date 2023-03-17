package com.example.demo.src.repository;

import com.example.demo.common.entity.BaseEntity.*;
import com.example.demo.src.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {


    Optional<Feed> findByIdAndState(Long id, State state);

    @EntityGraph(attributePaths = {"feedContentList","user"})
    @Query("select f from Feed f")
    List<Feed> findAllByState(State state, Pageable pageable);

    @EntityGraph(attributePaths = {"feedContentList","user"})
    @Query("select f from Feed f")
    List<Feed> findAllByUserIdAndState(Long userId, State state);


    @Query("select f from Feed f where (:email is null or f.user.email = :email) and " +
            "(:fromDate is null or f.createdAt >= :fromDate) and " +
            "(:toDate is null or f.createdAt <= :toDate) and " +
            "(:state is null or f.state = :state)")
    List<Feed> findByCriteria(
            @Param("email") String email,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("state") State state,
            Pageable pageable);

    Page<Feed> findByState(State state, Pageable pageable);
    Page<Feed> findAllByCreatedAt(LocalDateTime created, Pageable pageable);
    Page<Feed> findByUserId(Long userId, Pageable pageable);




}
