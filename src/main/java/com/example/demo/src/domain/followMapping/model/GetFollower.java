package com.example.demo.src.domain.followMapping.model;

import com.example.demo.src.entity.FollowMapping;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetFollower { //나를 팔로우 하는 사람 (그러면 팔로우에 내가 있어야지)
    private Long followerUserId;

    public GetFollower(FollowMapping followMapping){
        this.followerUserId=followMapping.getFollowerUser().getId();
    }
}
