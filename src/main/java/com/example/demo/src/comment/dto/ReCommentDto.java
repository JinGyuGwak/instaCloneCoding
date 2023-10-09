package com.example.demo.src.comment.dto;


import com.example.demo.src.comment.entity.ReComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ReCommentDto {
    protected String reComment;

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetReCommentRes extends ReCommentDto{
        private String username;
        private LocalDateTime updateDate;

        public GetReCommentRes(ReComment reComment){
            this.username=reComment.getUser().getName();
            this.reComment= reComment.getReCommentText();
            this.updateDate=reComment.getUpdatedAt();
        }
    }
    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateReCommentRes extends ReCommentDto{
        private Long reCommentId;
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class ReCommentRes extends ReCommentDto{
        private Long id; // ReCommentId
        private Long commentId;
        private Long userId;
        private String userName;
        public ReCommentRes(ReComment reComment){
            this.id=reComment.getId();
            this.commentId=reComment.getFeedComment().getId();
            this.userId=reComment.getUser().getId();
            this.userName=reComment.getUser().getName();
            this.reComment=reComment.getReCommentText();
        }

    }
}
