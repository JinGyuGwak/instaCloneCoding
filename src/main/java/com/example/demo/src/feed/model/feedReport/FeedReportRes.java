package com.example.demo.src.feed.model.feedReport;

import com.example.demo.src.feed.entity.FeedLike;
import com.example.demo.src.feed.entity.FeedReport;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
