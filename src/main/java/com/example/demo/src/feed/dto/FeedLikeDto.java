package com.example.demo.src.feed.dto;


import com.example.demo.src.feed.entitiy.Feed;
import com.example.demo.src.feed.entitiy.FeedContent;
import com.example.demo.src.feed.entitiy.FeedLike;
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
@NoArgsConstructor
public class FeedLikeDto {
    protected Long feedId;
    protected Long userId;

    public FeedLikeDto(FeedLike feedLike){
        this.feedId=feedLike.getFeed().getId();
        this.userId=feedLike.getUser().getId();
    }

    @Getter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetFeedLikeRes{
        private String userEmail;
        public GetFeedLikeRes(FeedLike feedLike){
            this.userEmail=feedLike.getUser().getEmail();
        }
    }
    @Getter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FeedDetailRes extends FeedLikeDto{
        private String postText;
        @Builder.Default
        private List<String> feedContentURL = new ArrayList<>();
        @Builder.Default
        private List<String> whoLikeThisFeed = new ArrayList<>();
        public FeedDetailRes(Feed feed){
            this.feedId=feed.getId();
            this.userId=feed.getUser().getId();
            this.postText= feed.getPostText();
            for(FeedContent feedContent : feed.getFeedContentList()){
                this.feedContentURL.add(feedContent.getPostURL());
            }
            for(FeedLike feedLike : feed.getFeedLikeList()){
                this.whoLikeThisFeed.add(feedLike.getUser().getEmail());
            }
        }

    }

}
