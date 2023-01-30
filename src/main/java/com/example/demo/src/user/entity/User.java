package com.example.demo.src.user.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.chatting.entity.ChattingRoom;
import com.example.demo.src.chatting.entity.ChattingText;
import com.example.demo.src.feed.entity.*;
import com.example.demo.src.followMapping.entity.FollowMapping;
import com.example.demo.src.myPage.entity.MyPage;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "USER") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class User extends BaseEntity {

    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private String password;

    private LocalDateTime birthday;

    private String whatSns; //어떤 SNS인가

    @OneToOne(mappedBy = "user")
    private MyPage myPage; //

    private LocalDateTime lastLoginTime;

    @Column(nullable = false)
    private boolean isOAuth;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    List<ChattingText> chattingTextList = new ArrayList<>();


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<Feed> feedList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<FeedBookMark> feedBookMarkList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<FeedComment> feedCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<FeedLike> feedLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<ReComment> reCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<CommentLike> commentLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "followUser", fetch = FetchType.LAZY) //팔로우 당하는 유저
    List<FollowMapping> followMappingList = new ArrayList<>();

    @OneToMany(mappedBy = "followerUser", fetch = FetchType.LAZY) //팔로우 하는 유저(팔로워가 됨)
    List<FollowMapping> followerMappingList = new ArrayList<>();


    @OneToMany(mappedBy = "sendUser", fetch = FetchType.LAZY)
    List<ChattingRoom> sendUserList = new ArrayList<>();

    @OneToMany(mappedBy = "receiveUser", fetch = FetchType.LAZY)
    List<ChattingRoom> receiveUserList = new ArrayList<>();



    @Builder
    public User(Long id, String email, String password, String name, String whatSns,boolean isOAuth) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.isOAuth = isOAuth;
        this.whatSns=whatSns;
    }
    public void lastLogin(){this.lastLoginTime=LocalDateTime.now();}

    public void updateName(String name) {
        this.name = name;
    }

    public void deleteUser() {
        this.state = State.INACTIVE;
    }
    public void adminDelete(){
        this.state=State.DENIED;
    }
}
