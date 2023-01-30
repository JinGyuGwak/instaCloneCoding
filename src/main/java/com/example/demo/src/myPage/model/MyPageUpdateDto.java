package com.example.demo.src.myPage.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyPageUpdateDto {
    private Long userId;
    private String name;
    private String nickname;
    private String introduction;

    @Builder
    public MyPageUpdateDto(Long userId, MyPageUpdateDto myPageUpdateDto){
        this.userId=userId;
        this.name= myPageUpdateDto.getName();
        this.nickname= myPageUpdateDto.getNickname();
        this.introduction= myPageUpdateDto.getIntroduction();
    }
}
