package com.example.demo.src.followMapping.controller;


import com.example.demo.src.followMapping.dto.FollowMappingDto.*;
import com.example.demo.src.followMapping.dto.FollowMappingDto.FollowerDto;
import com.example.demo.src.followMapping.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("{userId}")
    public ResponseEntity<List<FollowDto>> followMappingSearch(@PathVariable Long userId) {
        List<FollowDto> getFollowList = followService.followSearch(userId);
        return new ResponseEntity<>(getFollowList, HttpStatus.OK);
    }

    /**
     * {userId}내 팔로워 조회 (팔로워 조회)
     * [GET] /follow/{userId}
     */
    @GetMapping("/follower/{userId}")
    public ResponseEntity<List<FollowerDto>> followerSearch(@PathVariable Long userId) {
        List<FollowerDto> getFollowerList = followService.followerSearch(userId);
        return new ResponseEntity<>(getFollowerList,HttpStatus.OK);
    }

    /**
     * 팔로우 하기
     * [POST] /follow
     * 리퀘스트바디에 userId넣기
     */
    @PostMapping("")
    public ResponseEntity<PostFollowDto> followUser(@RequestBody PostFollowDto postFollowDto) {
        return new ResponseEntity<>(followService.followUser(
                postFollowDto.getFollowUserId(),
                postFollowDto.getFollowerUserId()),HttpStatus.OK);
    }

    /**
     * 팔로우 삭제
     * [DELETE] /follow
     * 팔로우매핑삭제는 STATE가 아닌 DB에서 바로 삭제
     * 리퀘스트 바디로 팔로우하는userId 팔로우당하는userId
     */
    @DeleteMapping("")
    public ResponseEntity<String> followDelete(@RequestBody PostFollowDto postFollowDto) {
        followService.followDelete(
                postFollowDto.getFollowUserId(),
                postFollowDto.getFollowerUserId());
        String result = "삭제 완료!!";
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
