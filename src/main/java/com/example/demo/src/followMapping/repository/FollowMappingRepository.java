package com.example.demo.src.followMapping.repository;

import com.example.demo.src.common.entity.BaseEntity;
import com.example.demo.src.common.entity.BaseEntity.State;
import com.example.demo.src.followMapping.entitiy.FollowMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowMappingRepository extends JpaRepository<FollowMapping,Long> {

    Optional<FollowMapping> findByFollowUserIdAndFollowerUserId(Long followUserId, Long followerUserId);
    List<FollowMapping> findByFollowUserIdAndState(Long followUserId, State state);
    List<FollowMapping> findByFollowerUserIdAndState(Long followerUserId, State state);
}
