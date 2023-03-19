package com.example.demo.src.controller;


import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.service.FollowService;
import com.example.demo.src.response.GetFollow;
import com.example.demo.src.response.GetFollower;
import com.example.demo.src.response.PostFollowRes;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/follow")
public class FollowController {
    private final FollowService followService;

    /**
     * 내가 팔로우 하는 사람 조회 (팔로우 조회)
     * [GET] /follow/{userId}
     */
    @ResponseBody
    @GetMapping("{userId}")
    public BaseResponse<List<GetFollow>> followMappingSearch(@PathVariable Long userId) {
        List<GetFollow> getFollowList = followService.followSearch(userId);
        return new BaseResponse<>(getFollowList);
    }

    /**
     * {userId}내 팔로워 조회 (팔로워 조회)
     * [GET] /follow/{userId}
     */
    @ResponseBody
    @GetMapping("/follower/{userId}")
    public BaseResponse<List<GetFollower>> followerSearch(@PathVariable Long userId) {
        List<GetFollower> getFollowerList = followService.followerSearch(userId);
        return new BaseResponse<>(getFollowerList);
    }

    /**
     * 팔로우 하기
     * [POST] /follow
     * 리퀘스트바디에 userId넣기
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostFollowRes> followUser(@RequestBody PostFollowDto postFollowDto) {
        PostFollowRes postFollowRes = followService.followUser(
                postFollowDto.getFollowUserId(),
                postFollowDto.getFollowerUserId());
        return new BaseResponse<>(postFollowRes);
    }

    /**
     * 팔로우 삭제
     * [DELETE] /follow
     * 팔로우매핑삭제는 STATE가 아닌 DB에서 바로 삭제
     * 리퀘스트 바디로 팔로우하는userId 팔로우당하는userId
     */
    @ResponseBody
    @DeleteMapping("")
    public BaseResponse<String> followDelete(@RequestBody PostFollowDto postFollowDto) {
        followService.followDelete(
                postFollowDto.getFollowUserId(),
                postFollowDto.getFollowerUserId());

        String result = "삭제 완료!!";
        return new BaseResponse<>(result);
    }

    @Getter
    private static class PostFollowDto{
        private Long followUserId; //팔로우 당하는 사람
        private Long followerUserId; // 팔로우 하는 사람(팔로워가 됨)

    }

}
