package com.example.demo.src.myPage.model;

import com.example.demo.src.myPage.entity.MyPage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageRequestRes {
    private Long userId;
    private String name;
    private String nickname;
    private String introduction;

    public MyPageRequestRes(MyPage myPage){
        this.userId=myPage.getUser().getId();
        this.name= myPage.getName();
        this.nickname= myPage.getNickname();
        this.introduction= myPage.getIntroduction();
    }
}
