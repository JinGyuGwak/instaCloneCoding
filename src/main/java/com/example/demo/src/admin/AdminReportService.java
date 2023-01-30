package com.example.demo.src.admin;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.admin.model.AdminReportRequestRes;
import com.example.demo.src.admin.model.AdminUserRequestRes;
import com.example.demo.src.feed.entity.FeedReport;
import com.example.demo.src.feed.repository.FeedReportRepository;
import com.example.demo.src.feed.repository.FeedRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class AdminReportService {
    private final FeedReportRepository feedReportRepository;
    private final FeedRepository feedRepository;


    //관리자페이지 신고 조회
    @Transactional(readOnly = true)
    public List<AdminReportRequestRes> getReport(Pageable pageable) {
        Page<FeedReport> feedReportList= feedReportRepository.findAll(pageable);
        List<AdminReportRequestRes> adminUserRequestResList = new ArrayList<>();
        for(FeedReport feedReport : feedReportList){
            AdminReportRequestRes a = new AdminReportRequestRes(feedReport);
            adminUserRequestResList.add(a);
        }
        return adminUserRequestResList;
    }
    //관리자페이지 신고 받아들여서 피드 삭제
    public void deleteFeedFromReport(Long feedReportId){
        FeedReport feedReport = feedReportRepository.findById(feedReportId)
                .orElseThrow(()->new BaseException(BaseResponseStatus.NOT_FIND_FEED));

        feedRepository.deleteById(feedReport.getFeed().getId());
    }


}
