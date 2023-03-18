package com.example.demo.src.response;

import com.example.demo.src.entity.Feed;
import com.example.demo.src.entity.FeedContent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetFeedRes {
    private Long id; //피드Id
    private String postText;
    private String username;
    private List<String> feedContentListURL = new ArrayList<>();
//  피드 컨텐츠는 나중에 추가하자

    public GetFeedRes(Feed feed) {
        this.id=feed.getId();
        this.postText=feed.getPostText();
        this.username=feed.getUser().getName();
        for(FeedContent feedContent : feed.getFeedContentList()){
            this.feedContentListURL.add(feedContent.getPostURL());
        }
    }
}