package com.example.demo.src.myPage.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.myPage.model.MyPageUpdateDto;
import com.example.demo.src.user.entity.User;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "MYPAGE") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class MyPage extends BaseEntity {
    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String name; //화면에선 id로표현

    @Column(length = 20)
    private String nickname;//화면에선 이름으로 표현함

    private String introduction; //소개글

    @OneToOne
    @JoinColumn(name = "userId", unique=true)
    private User user;

    public MyPage(User user, MyPageUpdateDto myPageUpdateDto){
        this.user=user;
        this.name= myPageUpdateDto.getName();
        this.nickname= myPageUpdateDto.getNickname();
        this.introduction= myPageUpdateDto.getIntroduction();
    }
    public void update(MyPageUpdateDto myPageUpdateDto){
        this.name= myPageUpdateDto.getName();
        this.nickname= myPageUpdateDto.getNickname();
        this.introduction= myPageUpdateDto.getIntroduction();
    }


}
