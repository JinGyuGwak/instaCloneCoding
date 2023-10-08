package com.example.demo.src.myPage.dto;


import com.example.demo.src.myPage.entitiy.MyPage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyPageDto {
    private Long userId;
    private String name;
    private String nickname;
    private String introduction;

    public MyPageDto(MyPage myPage){
        this.userId=myPage.getUser().getId();
        this.name= myPage.getName();
        this.nickname= myPage.getNickname();
        this.introduction= myPage.getIntroduction();
    }
}
