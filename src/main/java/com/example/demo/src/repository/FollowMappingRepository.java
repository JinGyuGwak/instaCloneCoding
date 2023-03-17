package com.example.demo.src.repository;

import com.example.demo.src.entity.FollowMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import static com.example.demo.common.entity.BaseEntity.*;

public interface FollowMappingRepository extends JpaRepository<FollowMapping,Long> {

    Optional<FollowMapping> findByFollowUserIdAndFollowerUserId(Long followUserId, Long followerUserId);
    List<FollowMapping> findByFollowUserIdAndState(Long followUserId, State state);
    List<FollowMapping> findByFollowerUserIdAndState(Long followerUserId, State state);
}
