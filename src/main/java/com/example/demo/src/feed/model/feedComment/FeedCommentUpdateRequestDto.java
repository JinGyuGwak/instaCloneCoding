package com.example.demo.src.feed.model.feedComment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class FeedCommentUpdateRequestDto {
    private String feedComment;

    @Builder
    public FeedCommentUpdateRequestDto(String feedComment){
        this.feedComment=feedComment;
    }
}
