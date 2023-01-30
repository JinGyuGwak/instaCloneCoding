package com.example.demo.src.followMapping.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostFollowDto {
    private Long followUserId; //팔로우 당하는 사람
    private Long followerUserId; // 팔로우 하는 사람(팔로워가 됨)

    public PostFollowDto(Long followUserId, Long followerUserId){
        this.followUserId=followUserId;
        this.followerUserId=followerUserId;
    }
}
