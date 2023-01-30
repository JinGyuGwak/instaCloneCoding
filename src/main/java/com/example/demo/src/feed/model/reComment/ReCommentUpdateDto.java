package com.example.demo.src.feed.model.reComment;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReCommentUpdateDto {
    private String reComment;

    @Builder
    public ReCommentUpdateDto(String reComment){
        this.reComment=reComment;
    }
}
