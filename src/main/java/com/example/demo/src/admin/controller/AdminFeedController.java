package com.example.demo.src.admin.controller;


import com.example.demo.src.common.entity.BaseEntity;
import com.example.demo.src.common.exceptions.BaseException;
import com.example.demo.src.common.response.ResponseEntityCustom;
import com.example.demo.src.admin.dto.response.AdminFeedRequestRes;
import com.example.demo.src.feed.dto.response.FeedDetailRes;
import com.example.demo.src.admin.service.AdminFeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/feed")
public class AdminFeedController {

    private final AdminFeedService adminFeedService;
    /**
     * 관리자 페이지 피드 조건 검색 //(무조건 필터가 있음)
     * [GET] /admin/feed/filter?email=&
     * @param
     * @return
     */
    @ResponseBody
    @GetMapping("/filter")
    public ResponseEntityCustom<List<AdminFeedRequestRes>> getFeedFilter(
            @PageableDefault(page=0, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "email", required = false) String email, //email
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
            @RequestParam(value = "state", required = false) BaseEntity.State state){
        try{
            LocalDateTime fromDateTime = fromDate != null ? fromDate.atStartOfDay() : null;
            LocalDateTime toDateTime = toDate != null ? toDate.atStartOfDay() : null;
            List<AdminFeedRequestRes> adminFeedRequestRes = adminFeedService.getFeed(email,fromDateTime,toDateTime,state,pageable);
            return new ResponseEntityCustom<>(adminFeedRequestRes);

        }catch(BaseException exception){
            return new ResponseEntityCustom<>((exception.getStatus()));
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
    public ResponseEntityCustom<FeedDetailRes> getFeedDetail(@PathVariable Long feedId){
        FeedDetailRes feedDetailRes = adminFeedService.getFeedDetail(feedId);
        return new ResponseEntityCustom<>(feedDetailRes);
    }

    @ResponseBody
    @DeleteMapping("{feedId}")
    public ResponseEntityCustom<String> adminDeleteFeed(@PathVariable Long feedId){
        adminFeedService.adminDeleteFeed(feedId);
        String result = "삭제완료!";
        return new ResponseEntityCustom<>(result);
    }


}
