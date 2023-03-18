package com.example.demo.src.controller;


import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.response.AdminReportRequestRes;
import com.example.demo.src.service.AdminReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/report")
public class AdminReportController {
    private final AdminReportService adminReportService;
    /**
     * 관리자 페이지 신고화면
     * [GET] /admin/report
     * @param
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<AdminReportRequestRes>> getReport(
            @PageableDefault(page=0, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable){
        List<AdminReportRequestRes> adminReportRequestRes = adminReportService.getReport(pageable);
        return new BaseResponse<>(adminReportRequestRes);
    }
    @ResponseBody
    @DeleteMapping("{feedReportId}")
    public BaseResponse<String> deleteFeedFromReport(@PathVariable Long feedReportId){
        adminReportService.deleteFeedFromReport(feedReportId);
        String result = "피드를 삭제하였습니다.";
        return new BaseResponse<>(result);

    }

}
