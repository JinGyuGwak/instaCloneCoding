package com.example.demo.src.feed.dto.response;

import com.example.demo.src.feed.entitiy.FeedReport;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedReportRes {
    private Long feedId;
    private Long userId;
    private String reason;

    public FeedReportRes(FeedReport feedReport){
        this.feedId=feedReport.getFeed().getId();
        this.userId=feedReport.getUser().getId();
        this.reason= feedReport.getReason();
    }
}
