package com.example.demo.src.feed.model.feedReport;

import com.example.demo.src.feed.entity.FeedComment;
import com.example.demo.src.feed.entity.FeedReport;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class FeedReportDto {
    private Long userId;
    private Long feedId;
    private String reason;

    public FeedReportDto(FeedReport feedReport){
        this.userId=feedReport.getUser().getId();
        this.feedId=feedReport.getFeed().getId();
        this.reason= feedReport.getReason();
    }
}
