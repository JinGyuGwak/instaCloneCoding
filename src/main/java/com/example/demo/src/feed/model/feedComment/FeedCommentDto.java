package com.example.demo.src.feed.model.feedComment;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class FeedCommentDto {
    private Long userId;
    private String feedComment;

    public FeedCommentDto(Long userId, String feedComment)
    {
        this.userId=userId;
        this.feedComment=feedComment;
    }

}
