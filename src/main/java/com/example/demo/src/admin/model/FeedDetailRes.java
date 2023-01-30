package com.example.demo.src.admin.model;

import com.example.demo.src.feed.entity.Feed;
import com.example.demo.src.feed.entity.FeedContent;
import com.example.demo.src.feed.entity.FeedLike;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class FeedDetailRes {
    private Long id;
    private Long userId;
    private String postText;

    private List<String> feedContentURL = new ArrayList<>();
    private List<String> whoLikeThisFeed = new ArrayList<>();

    public FeedDetailRes(Feed feed){
        this.id=feed.getId();
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
