package com.example.demo.src.feed.controller;


import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.feed.dto.request.FeedCreateRequestDto;
import com.example.demo.src.feed.dto.response.FeedReportRes;
import com.example.demo.src.feed.dto.response.GetFeedRes;
import com.example.demo.src.feed.dto.response.PostFeedRes;
import com.example.demo.src.user.dto.response.UpdateFeedRes;
import com.example.demo.src.feed.service.FeedService;
import com.example.demo.src.user.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/feed")
public class FeedController {

    private final FeedService feedService;
    private final UserService userService;

    /**
     * 피드생성(S3에 이미지 저장)
     * @param userId
     * @param files
     * @param postText
     * @return
     */
    @PostMapping("")
    public BaseResponse<PostFeedRes> updateUserImage(
            @RequestParam(value = "userId") Long userId,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(value = "postText", required = false) String postText) throws Exception {
        FeedCreateRequestDto saveDto = new FeedCreateRequestDto(userService.searchUserById(userId),postText);
        PostFeedRes postFeedRes = feedService.upload(saveDto,files);

        return new BaseResponse<>(postFeedRes);
    }
    /**
     * 피드 삭제구현 API
     * 피드 삭제하면 DB에서 없앨거임
     */
    @ResponseBody
    @DeleteMapping("{feedId}")
    public BaseResponse<String> deleteFeed(@PathVariable Long feedId){
        feedService.deleteFeed(feedId);

        String result = "삭제 완료!!";
        return new BaseResponse<>(result);
    }
    /**
     * 피드 업데이트(텍스트 변경)
     * [PATCH] /feed/{id}
     */
    @ResponseBody
    @PatchMapping("{feedId}") //피드 아이디
    public BaseResponse<UpdateFeedRes> updateFeed(@PathVariable Long feedId,
                                                  @RequestBody FeedUpdateRequestDto requestDto) {

        UpdateFeedRes updateFeedRes = feedService.updateFeed(feedId,requestDto.getPostText());
        return new BaseResponse<>(updateFeedRes);
    }
    /**
     * 피드조회 API
     * [GET] /feed
     * @return BaseResponse<GetFeedRes>
     *
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetFeedRes>> searchAllFeed(
            @PageableDefault(page = 0, size=20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable){
        List<GetFeedRes> getFeedRes = feedService.searchAllFeed(pageable);
        return new BaseResponse<>(getFeedRes);
    }
    /**
     * 유저id로 피드조회 API
     * [GET] /feed/{userId}
     */
    @ResponseBody
    @GetMapping("{userId}")
    public BaseResponse<List<GetFeedRes>> searchUserFeed(@PathVariable Long userId){

        List<GetFeedRes> getFeedRes = feedService.searchUserFeed(userId);
        return new BaseResponse<>(getFeedRes);
    }

    /**
     * 피드 신고
     * [POST] /feedreport/
     * 바디값으로는 유저id를 받는다
     */
    @ResponseBody
    @PostMapping("/feedreport")
    public BaseResponse<FeedReportRes> feedReport(@RequestBody FeedReportDto feedReportDto) {
        FeedReportRes feedReportRes = feedService.feedReport(
                feedReportDto.getUserId(),
                feedReportDto.feedId,
                feedReportDto.getReason());

        return new BaseResponse<>(feedReportRes);
    }

    @Getter
    private static class FeedUpdateRequestDto{
        private String postText;
    }
    @Getter
    private static class FeedReportDto{
        private Long userId;
        private Long feedId;
        private String reason;

    }


}
