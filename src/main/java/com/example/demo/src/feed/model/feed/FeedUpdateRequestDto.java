package com.example.demo.src.feed.model.feed;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class FeedUpdateRequestDto {
    private String postText;

    @Builder
    public FeedUpdateRequestDto(String postText){
        this.postText=postText;
    }
}
