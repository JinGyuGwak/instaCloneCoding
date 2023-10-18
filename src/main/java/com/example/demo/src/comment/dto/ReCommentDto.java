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
    public static class GetReCommentRes{
        private String username;
        private LocalDateTime updateDate;
        private String reComment;

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
    public static class UpdateReCommentRes{
        private Long reCommentId;
        private String reComment;

    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    public static class ReCommentRes{
        private Long id; // ReCommentId
        private Long commentId;
        private Long userId;
        private String userName;
        private String reComment;
        public ReCommentRes(ReComment reComment){
            this.id=reComment.getId();
            this.commentId=reComment.getFeedComment().getId();
            this.userId=reComment.getUser().getId();
            this.userName=reComment.getUser().getName();
            this.reComment=reComment.getReCommentText();
        }

    }
}
