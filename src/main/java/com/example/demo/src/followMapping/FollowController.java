package com.example.demo.src.followMapping;


import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.followMapping.model.GetFollow;
import com.example.demo.src.followMapping.model.GetFollower;
import com.example.demo.src.followMapping.model.PostFollowDto;
import com.example.demo.src.followMapping.model.PostFollowRes;
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
        PostFollowRes postFollowRes = followService.followUser(postFollowDto);
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
        followService.followDelete(postFollowDto);

        String result = "삭제 완료!!";
        return new BaseResponse<>(result);
    }
}
