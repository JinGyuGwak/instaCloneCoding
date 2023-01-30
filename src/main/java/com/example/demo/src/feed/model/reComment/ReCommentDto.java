package com.example.demo.src.feed.model.reComment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReCommentDto {
    private Long userId;
    private String reComment;

    public ReCommentDto(Long userId, String reComment)
    {
        this.userId=userId;
        this.reComment=reComment;
    }
}
