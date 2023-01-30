package com.example.demo.src.feed;


import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.feed.model.feed.*;
import com.example.demo.src.feed.model.feedReport.FeedReportDto;
import com.example.demo.src.feed.model.feedReport.FeedReportRes;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.UserService;
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
        FeedCreateRequestDto saveDto = FeedCreateRequestDto.builder()
                .user(userService.searchUserById(userId))
                .postText(postText)
                .build();
        PostFeedRes postFeedRes = feedService.upload(saveDto,files);

        return new BaseResponse<>(postFeedRes);
    }
    /**
     * 피드 삭제구현 API
     * 피드 삭제하면 DB에서 없앨거임
     */
    @ResponseBody
    @DeleteMapping("{feedId}")
    public BaseResponse<String> deleteFeed(@PathVariable("feedId") Long id){
        feedService.deleteFeed(id);

        String result = "삭제 완료!!";
        return new BaseResponse<>(result);
    }
    /**
     * 피드 업데이트(텍스트 변경)
     * [PATCH] /feed/{id}
     */
    @ResponseBody
    @PatchMapping("{id}") //피드 아이디
    public BaseResponse<UpdateFeedRes> updateFeed(@PathVariable Long id,
                                                  @RequestBody FeedUpdateRequestDto requestDto) {

        UpdateFeedRes updateFeedRes = feedService.updateFeed(id,requestDto);
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
            @PageableDefault(page = 0, size=5, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable){
        List<GetFeedRes> getFeedRes = feedService.searchAllFeed(pageable);
        return new BaseResponse<>(getFeedRes);
    }
    /**
     * 유저id로 피드조회 API
     * [GET] /feed/{userId}
     */
    @ResponseBody
    @GetMapping("{id}")
    public BaseResponse<List<GetFeedRes>> searchUserFeed(@PathVariable Long id){

        List<GetFeedRes> getFeedRes = feedService.searchUserFeed(id);
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
        FeedReportRes feedReportRes = feedService.feedReport(feedReportDto);

        return new BaseResponse<>(feedReportRes);
    }


}
