package com.example.demo.src.comment.dto;


import com.example.demo.src.comment.entity.FeedComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    protected String commentText;


    @Getter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FeedCommentRes extends CommentDto{
        private Long id; // FeedCommentId
        private Long feedId;
        private Long userId;
        private String username;
        public FeedCommentRes(FeedComment feedComment){
            this.id= feedComment.getId();
            this.feedId=feedComment.getFeed().getId();
            this.userId= feedComment.getUser().getId();
            this.username=feedComment.getUser().getName();
            this.commentText= feedComment.getCommentText();
        }
    }
    @Getter
    @SuperBuilder
    @AllArgsConstructor
    public static class FeedCommentUpdateRequestDto extends CommentDto{
        //CommentDto 를 사용하지 않은 이유는 추후에 commentText 뿐 아니라 새로운 필드가 추가 될 가능성이 있기 때문에 나눴음
    }
    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateFeedCommentRes extends CommentDto {
        private Long commentId;
    }


    @Getter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FeedCommentDto extends CommentDto{
        private Long userId;
    }
    @Getter
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GetFeedCommentRes extends CommentDto{
        private String username;
        private LocalDateTime updateDate;
        public GetFeedCommentRes(FeedComment feedComment){
            this.username=feedComment.getUser().getName();
            this.commentText= feedComment.getCommentText();
            this.updateDate=feedComment.getUpdatedAt();
        }
    }

}
