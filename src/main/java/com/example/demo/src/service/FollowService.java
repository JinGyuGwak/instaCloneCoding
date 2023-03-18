package com.example.demo.src.service;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.repository.FollowMappingRepository;
import com.example.demo.src.entity.FollowMapping;
import com.example.demo.src.request.GetFollow;
import com.example.demo.src.request.GetFollower;
import com.example.demo.src.response.PostFollowRes;
import com.example.demo.src.func.FuncUser;
import com.example.demo.src.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.common.entity.BaseEntity.State.*;
import static com.example.demo.common.response.BaseResponseStatus.*;


@Transactional
@RequiredArgsConstructor
@Service
public class FollowService {
    private final FuncUser funcUser;
    private final FollowMappingRepository followMappingRepository;

    //{id}가 팔로우 하는 사람 조회
    @Transactional
    public List<GetFollow> followSearch(Long userId){
        return followMappingRepository.findByFollowerUserIdAndState(userId, ACTIVE)
                .stream()
                .map(GetFollow::new)
                .collect(Collectors.toList());
    }
    //{id}의 팔로워 조회 (팔로우에 내가 있으면 됨)
    @Transactional
    public List<GetFollower> followerSearch(Long userId){
        return followMappingRepository.findByFollowUserIdAndState(userId, ACTIVE)
                .stream()
                .map(GetFollower::new)
                .collect(Collectors.toList());
    }

    //팔로우 요청
    @Transactional
    public PostFollowRes followUser(Long followUserId, Long followerUserId){
        User followUser = funcUser.findUserByIdAndState(followUserId);
        User followerUser = funcUser.findUserByIdAndState(followerUserId);

        FollowMapping followMapping = new FollowMapping(followUser,followerUser);
        followMappingRepository.save(followMapping);
        return new PostFollowRes(followMapping);
    }

    //팔로우 삭제(취소)
    @Transactional
    public void followDelete(Long followUserId, Long followerUserId){
        FollowMapping followMapping = followMappingRepository.
                findByFollowUserIdAndFollowerUserId(followUserId,followerUserId)
                .orElseThrow(()->new BaseException(NOT_FIND_USER));
        followMappingRepository.delete(followMapping);
    }
}
