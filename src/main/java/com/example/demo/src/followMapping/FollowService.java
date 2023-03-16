package com.example.demo.src.followMapping;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.followMapping.entity.FollowMapping;
import com.example.demo.src.followMapping.model.GetFollow;
import com.example.demo.src.followMapping.model.GetFollower;
import com.example.demo.src.followMapping.model.PostFollowDto;
import com.example.demo.src.followMapping.model.PostFollowRes;
import com.example.demo.src.func.FuncUser;
import com.example.demo.src.user.entity.User;
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
    public PostFollowRes followUser(PostFollowDto postFollowDto){
        User followUser = funcUser.findUserByIdAndState(postFollowDto.getFollowUserId());
        User followerUser = funcUser.findUserByIdAndState(postFollowDto.getFollowerUserId());

        FollowMapping followMapping = new FollowMapping(followUser,followerUser);
        followMappingRepository.save(followMapping);
        return new PostFollowRes(followMapping);
    }

    //팔로우 삭제(취소)
    @Transactional
    public void followDelete(PostFollowDto postFollowDto){
        FollowMapping followMapping = followMappingRepository.
                findByFollowUserIdAndFollowerUserId(postFollowDto.getFollowUserId(),postFollowDto.getFollowerUserId())
                .orElseThrow(()->new BaseException(NOT_FIND_USER));
        followMappingRepository.delete(followMapping);
    }
}
