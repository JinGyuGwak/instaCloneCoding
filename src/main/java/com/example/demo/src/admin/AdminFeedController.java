package com.example.demo.src.admin;


import com.example.demo.common.entity.BaseEntity;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.admin.model.AdminFeedRequestRes;
import com.example.demo.src.admin.model.AdminUserRequestRes;
import com.example.demo.src.admin.model.FeedDetailRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/feed")
public class AdminFeedController {

    private final AdminFeedService adminFeedService;
    /**
     * 관리자 페이지 피드 화면
     * [GET] /admin/feed
     * @param
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<AdminFeedRequestRes>> getFeed(
            @PageableDefault(page=0, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable){
        List<AdminFeedRequestRes> adminFeedRequestRes = adminFeedService.getFeed(pageable);
        return new BaseResponse<>(adminFeedRequestRes);
    }
    /**
     * 관리자 페이지 피드 조건 검색 //(무조건 필터가 있음)
     * [GET] /admin/feed/filter?email=&
     * @param
     * @return
     */
    @ResponseBody
    @GetMapping("/filter")
    public BaseResponse<List<AdminFeedRequestRes>> getFeedFilter(
            @PageableDefault(page=0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "email", required = false) String email, //email
            @RequestParam(value = "created", required = false) LocalDateTime created,
            @RequestParam(value = "state", required = false) BaseEntity.State state){
        try{
            if(email==null && created==null){
                List<AdminFeedRequestRes> adminFeedRequestRes = adminFeedService.getFeedFilterState(state,pageable);
                return new BaseResponse<>(adminFeedRequestRes);

            } else if(created==null && state==null){
                List<AdminFeedRequestRes> adminFeedRequestRes = adminFeedService.getFeedFilterEmail(email,pageable);
                return new BaseResponse<>(adminFeedRequestRes);

            } else if(state==null){
                List<AdminFeedRequestRes> adminFeedRequestRes = adminFeedService.getFeedFilterCreated(created,pageable);
                return new BaseResponse<>(adminFeedRequestRes);
            }
            List<AdminFeedRequestRes> adminFeedRequestRes = adminFeedService.getFeed(pageable);
            return new BaseResponse<>(adminFeedRequestRes);

        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 피드 상세조회
     * [GET] /admin/feed/{feedId}
     * @param feedId
     * @return
     */
    @ResponseBody
    @GetMapping("{feedId}")
    public BaseResponse<FeedDetailRes> getFeedDetail(@PathVariable Long feedId){
        FeedDetailRes feedDetailRes = adminFeedService.getFeedDetail(feedId);
        return new BaseResponse<>(feedDetailRes);
    }

    @ResponseBody
    @DeleteMapping("{feedId}")
    public BaseResponse<String> adminDeleteFeed(@PathVariable Long feedId){
        adminFeedService.adminDeleteFeed(feedId);
        String result = "삭제완료!";
        return new BaseResponse<>(result);
    }


}
