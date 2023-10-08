package com.example.demo.src.user.dto;


import com.example.demo.src.comment.entity.FeedComment;
import com.example.demo.src.feed.entitiy.Feed;
import com.example.demo.src.feed.entitiy.FeedLike;
import com.example.demo.src.user.entitiy.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @Email @NotNull
    protected String email;
    protected String password;
    protected String name;


    @Getter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDetailRes extends UserDto{
        protected Long id;
        private LocalDateTime lastLoginTime;
        @Builder.Default
        private List<Long> feedIdList = new ArrayList<>();
        @Builder.Default
        private List<Long> feedCommentId = new ArrayList<>(); //해당 유저가 남긴 커멘트의 피드id
        @Builder.Default
        private List<String> feedCommentList = new ArrayList<>(); //해당 유저가 남긴 커멘트
        @Builder.Default
        private List<Long> feedLikeList = new ArrayList<>(); //해당 유저가 남긴 좋아요
        @Builder
        public UserDetailRes(User user) {
            this.id = user.getId();
            this.email=user.getEmail();
            this.name=user.getName();
            this.lastLoginTime = user.getLastLoginTime();
            for (Feed feed : user.getFeedList()) {
                this.feedIdList.add(feed.getId());
            }
            for (FeedComment feedComment : user.getFeedCommentList()) { //해당 유저가 남긴 커멘트들
                this.feedCommentId.add(feedComment.getFeed().getId());
                this.feedCommentList.add(feedComment.getCommentText());
            }
            for (FeedLike feedLike : user.getFeedLikeList()) {
                this.feedLikeList.add(feedLike.getFeed().getId()); //해당 유저가 좋아요 한 피드
            }
        }
    }
    @Getter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostUserRes extends UserDto{
        private Long id;
        private String jwt;
    }

    @Getter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetUserRes extends UserDto{
        private Long id;

        public GetUserRes(User user){
            this.id=user.getId();
            this.email=user.getEmail();
            this.name=user.getName();
        }

    }

}
