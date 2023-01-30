package com.example.demo.src.followMapping;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.followMapping.entity.FollowMapping;
import com.example.demo.src.followMapping.model.GetFollow;
import com.example.demo.src.followMapping.model.GetFollower;
import com.example.demo.src.followMapping.model.PostFollowDto;
import com.example.demo.src.followMapping.model.PostFollowRes;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.common.entity.BaseEntity.State.*;
import static com.example.demo.common.response.BaseResponseStatus.*;


@Transactional
@RequiredArgsConstructor
@Service
public class FollowService {
    private final UserRepository userRepository;
    private final FollowMappingRepository followMappingRepository;

    //{id}가 팔로우 하는 사람 조회
    public List<GetFollow> followSearch(Long userId){
        List<FollowMapping> followMappingList = followMappingRepository.findByFollowerUserIdAndState(userId,ACTIVE);
        List<GetFollow> getFollowList = new ArrayList<>();
        for(FollowMapping followMapping : followMappingList){
            GetFollow a = new GetFollow(followMapping);
            getFollowList.add(a);
        }
        return getFollowList;
    }
    //{id}의 팔로워 조회 (팔로우에 내가 있으면 됨)
    public List<GetFollower> followerSearch(Long userId){
        List<FollowMapping> followerMappingList = followMappingRepository.findByFollowUserIdAndState(userId,ACTIVE);
        List<GetFollower> getFollowerList = new ArrayList<>();
        for(FollowMapping followMapping : followerMappingList){
            GetFollower a = new GetFollower(followMapping);
            getFollowerList.add(a);
        }
        return getFollowerList;
    }

    //팔로우 요청
    public PostFollowRes followUser(PostFollowDto postFollowDto){
        User followUser = userRepository.findByIdAndState(postFollowDto.getFollowUserId(), ACTIVE)
                .orElseThrow(()->new BaseException(NOT_FIND_USER));
        User followerUser = userRepository.findByIdAndState(postFollowDto.getFollowerUserId(), ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_USER));

        FollowMapping followMapping = new FollowMapping(followUser,followerUser);
        followMappingRepository.save(followMapping);
        return new PostFollowRes(followMapping);
    }
    //팔로우 취소
    public void followDelete(PostFollowDto postFollowDto){
        FollowMapping followMapping = followMappingRepository.
                findByFollowUserIdAndFollowerUserId(postFollowDto.getFollowUserId(),postFollowDto.getFollowerUserId())
                .orElseThrow(()->new BaseException(NOT_FIND_USER));
        followMappingRepository.delete(followMapping);
    }
}
