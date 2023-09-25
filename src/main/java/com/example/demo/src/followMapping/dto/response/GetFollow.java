package com.example.demo.src.followMapping.dto.response;

import com.example.demo.src.followMapping.entitiy.FollowMapping;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class GetFollow { //내가 팔로우 하는 사람   (그러면 팔로워에 내가 있어야지)
    private Long followUserId;

    public GetFollow(FollowMapping followMapping){
        this.followUserId=followMapping.getFollowUser().getId();
    }
}
