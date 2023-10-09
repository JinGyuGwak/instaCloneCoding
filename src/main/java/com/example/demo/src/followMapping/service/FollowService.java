package com.example.demo.src.followMapping.service;

import com.example.demo.src.followMapping.dto.FollowMappingDto.*;
import com.example.demo.src.followMapping.repository.FollowMappingRepository;
import com.example.demo.src.followMapping.entitiy.FollowMapping;
import com.example.demo.src.util.FuncUser;
import com.example.demo.src.user.entitiy.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.src.common.entity.BaseEntity.State.ACTIVE;


@RequiredArgsConstructor
@Service
public class FollowService {
    private final FuncUser funcUser;
    private final FollowMappingRepository followMappingRepository;

    //{id}가 팔로우 하는 사람 조회
    @Transactional(readOnly = true)
    public List<FollowDto> followSearch(Long userId){
        return followMappingRepository.findByFollowerUserIdAndState(userId, ACTIVE)
                .stream()
                .map(FollowDto::new)
                .collect(Collectors.toList());
    }
    //{id}의 팔로워 조회 (팔로우에 내가 있으면 됨)
    @Transactional(readOnly = true)
    public List<FollowerDto> followerSearch(Long userId){
        return followMappingRepository.findByFollowUserIdAndState(userId, ACTIVE)
                .stream()
                .map(FollowerDto::new)
                .collect(Collectors.toList());
    }

    //팔로우 요청
    @Transactional
    public PostFollowDto followUser(Long followUserId, Long followerUserId){
        User followUser = funcUser.findUserByIdAndState(followUserId);
        User followerUser = funcUser.findUserByIdAndState(followerUserId);

        FollowMapping followMapping = new FollowMapping(followUser,followerUser);
        followMappingRepository.save(followMapping);
        return new PostFollowDto(followMapping);
    }

    //팔로우 삭제(취소)
    @Transactional
    public void followDelete(Long followUserId, Long followerUserId){
        FollowMapping followMapping = followMappingRepository.
                findByFollowUserIdAndFollowerUserId(followUserId,followerUserId)
                .orElseThrow(()->new IllegalArgumentException("일치하는 유저가 없습니다."));
        followMappingRepository.delete(followMapping);
    }
}
