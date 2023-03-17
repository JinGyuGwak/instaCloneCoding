package com.example.demo.src.domain.followMapping.model;


import com.example.demo.src.entity.FollowMapping;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostFollowRes {
    private Long followUserId; //팔로우 당하는 사람
    private Long followerUserId; // 팔로우 하는 사람(팔로워가 됨)

    public PostFollowRes(FollowMapping followMapping){
        this.followUserId=followMapping.getFollowUser().getId();
        this.followerUserId=followMapping.getFollowerUser().getId();
    }
}
