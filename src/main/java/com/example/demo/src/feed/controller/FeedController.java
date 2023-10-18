package com.example.demo.src.feed.controller;


import com.example.demo.src.feed.dto.FeedDto;
import com.example.demo.src.feed.dto.FeedDto.*;
import com.example.demo.src.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/feed")
public class FeedController {
    private final FeedService feedService;
    /**
     * 피드생성(S3에 이미지 저장)
     * @param userId
     * @param files
     * @param postText
     * @return
     */
    @PostMapping("")
    public ResponseEntity<PostFeedRes> updateUserImage(
            @RequestParam(value = "userId") Long userId,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "postText", required = false) String postText) throws Exception {
        return new ResponseEntity<>(feedService.upload(userId,postText,files), HttpStatus.OK);
    }
    /**
     * 피드 삭제구현 API
     * 피드 삭제하면 DB에서 없앨거임
     */
    @DeleteMapping("{feedId}")
    public ResponseEntity<String> deleteFeed(@PathVariable Long feedId){
        feedService.deleteFeed(feedId);
        String result = "삭제 완료!!";
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
    /**
     * 피드 업데이트(텍스트 변경)
     * [PATCH] /feed/{id}
     */
    @PatchMapping("") //피드 아이디
    public ResponseEntity<UpdateFeedDto> updateFeed(@RequestBody UpdateFeedDto requestDto) {
        return new ResponseEntity<>(feedService.updateFeed(requestDto),HttpStatus.OK);
    }
    /**
     * 피드조회 API
     * [GET] /feed
     * @return BaseResponse<GetFeedRes>
     *
     */
    @GetMapping("")
    public ResponseEntity<List<GetFeedRes>> searchAllFeed(
            @PageableDefault(page = 0, size=20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        return new ResponseEntity<>(feedService.searchAllFeed(pageable),HttpStatus.OK);
    }
    /**
     * 유저id로 피드조회 API
     * [GET] /feed/{userId}
     */
    @GetMapping("{userId}")
    public ResponseEntity<List<GetFeedRes>> searchUserFeed(@PathVariable Long userId){
        return new ResponseEntity<>(feedService.searchUserFeed(userId),HttpStatus.OK);
    }

    /**
     * 피드 신고
     * [POST] /feedreport/
     * 바디값으로는 유저id를 받는다
     */
    @PostMapping("/feedreport")
    public ResponseEntity<FeedReportDto> feedReport(@RequestBody FeedReportDto feedReportDto){
        return new ResponseEntity<>(feedService.feedReport(feedReportDto),HttpStatus.OK);
    }
}
