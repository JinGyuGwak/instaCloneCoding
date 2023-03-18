package com.example.demo.src.response;


import com.example.demo.src.entity.FeedReport;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminReportRequestRes {
    private Long userId;
    private String userEmail;
    private Long feedId;
    private String reportReason;

    public AdminReportRequestRes(FeedReport feedReport){
        this.userId=feedReport.getUser().getId();
        this.userEmail=feedReport.getUser().getEmail();
        this.feedId=feedReport.getFeed().getId();
        this.reportReason=feedReport.getReason();
    }


}
