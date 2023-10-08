package com.example.demo.src.followMapping.entitiy;

import com.example.demo.src.common.entity.BaseEntity;
import com.example.demo.src.user.entitiy.User;
import lombok.*;

import javax.persistence.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "FOLLOWMAPPING") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class FollowMapping extends BaseEntity {
    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "followUserId")
    private User followUser; //팔로우 당하는 유저

    @ManyToOne
    @JoinColumn(name = "followerUserId")
    private User followerUser; //팔로우 하는 유저 (팔로워가 됨)

    public FollowMapping(User followUser, User followerUser){
        this.followUser=followUser;
        this.followerUser=followerUser;
    }


}
