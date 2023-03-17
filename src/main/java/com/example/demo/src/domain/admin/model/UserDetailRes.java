package com.example.demo.src.domain.admin.model;

import com.example.demo.src.entity.Feed;
import com.example.demo.src.entity.FeedComment;
import com.example.demo.src.entity.FeedLike;
import com.example.demo.src.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class UserDetailRes {
    private Long id;
    private LocalDateTime lastLoginTime;

    private List<Long> feedIdList = new ArrayList<>();

    private List<Long> feedCommentId = new ArrayList<>(); //해당 유저가 남긴 커멘트의 피드id
    private List<String> feedCommentList = new ArrayList<>(); //해당 유저가 남긴 커멘트


    private List<Long> feedLikeList = new ArrayList<>(); //해당 유저가 남긴 좋아요

    public UserDetailRes(User user){
        this.id=user.getId();
        this.lastLoginTime=user.getLastLoginTime();
        for(Feed feed: user.getFeedList()){
            this.feedIdList.add(feed.getId());
        }
        for(FeedComment feedComment : user.getFeedCommentList()){ //해당 유저가 남긴 커멘트들
            this.feedCommentId.add(feedComment.getFeed().getId());
            this.feedCommentList.add(feedComment.getCommentText());
        }
        for(FeedLike feedLike : user.getFeedLikeList()){
            this.feedLikeList.add(feedLike.getFeed().getId()); //해당 유저가 좋아요 한 피드
        }











    }

}
