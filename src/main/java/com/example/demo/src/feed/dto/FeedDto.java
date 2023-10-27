package com.example.demo.src.feed.dto;


import com.example.demo.src.feed.entitiy.Feed;
import com.example.demo.src.feed.entitiy.FeedContent;
import com.example.demo.src.feed.entitiy.FeedReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor
public class FeedDto {

    @Getter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetFeedRes {
        private Long feedId; //피드Id
        private String username;
        private String feedText;

        @Builder.Default
        private List<String> feedContentListURL = new ArrayList<>();

        public GetFeedRes(Feed feed){
            if(this.feedContentListURL == null){
                this.feedContentListURL=new ArrayList<>();
            }
            this.feedId=feed.getId();
            this.feedText=feed.getPostText();
            this.username=feed.getUser().getName();
            for(FeedContent feedContent : feed.getFeedContentList()) {
                this.feedContentListURL.add(feedContent.getPostURL());
            }
        }
    }

    @Getter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostFeedRes {
        private Long feedId; //postId
        private Long userId; //userId

        public PostFeedRes(Feed feed){
            this.feedId = feed.getId();
            this.userId=feed.getUser().getId();
        }
    }
    @Getter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateFeedDto {
        private String updateText;
        private Long feedId; //postId
    }

    @Getter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FeedReportDto{
        private Long userId;
        private Long feedId;
        private String reason;

        public FeedReportDto(FeedReport feedReport){
            this.feedId=feedReport.getFeed().getId();
            this.userId=feedReport.getUser().getId();
            this.reason= feedReport.getReason();
        }
    }
}
