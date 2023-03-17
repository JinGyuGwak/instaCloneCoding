package com.example.demo.src.domain.feed.model.feed;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFeedRes {
    private Long id; //postId
    private Long userId; //userId

    public UpdateFeedRes(Long id) {
        this.id = id;
    }

}
