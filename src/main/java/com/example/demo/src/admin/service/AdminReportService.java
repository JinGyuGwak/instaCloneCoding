package com.example.demo.src.admin.service;

import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponseStatus;
import com.example.demo.src.admin.dto.response.AdminReportRequestRes;
import com.example.demo.src.feed.entitiy.FeedReport;
import com.example.demo.src.feed.repository.FeedReportRepository;
import com.example.demo.src.feed.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class AdminReportService {
    private final FeedReportRepository feedReportRepository;
    private final FeedRepository feedRepository;


    //관리자페이지 신고 조회
    @Transactional(readOnly = true)
    public List<AdminReportRequestRes> getReport(Pageable pageable) {
        return feedReportRepository.findAll(pageable)
                .stream()
                .map(AdminReportRequestRes::new)
                .collect(Collectors.toList());

    }
    //관리자페이지 신고 받아들여서 피드 삭제
    @Transactional
    public void deleteFeedFromReport(Long feedReportId){
        FeedReport feedReport = feedReportRepository.findById(feedReportId)
                .orElseThrow(()->new BaseException(BaseResponseStatus.NOT_FIND_FEED));

        feedRepository.deleteById(feedReport.getFeed().getId());
    }


}
