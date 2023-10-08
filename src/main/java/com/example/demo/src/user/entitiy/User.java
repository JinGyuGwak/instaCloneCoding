package com.example.demo.src.user.entitiy;

import com.example.demo.src.common.entity.BaseEntity;
import com.example.demo.src.chatting.entitiy.ChattingRoom;
import com.example.demo.src.chatting.entitiy.ChattingText;
import com.example.demo.src.comment.entity.CommentLike;
import com.example.demo.src.comment.entity.FeedComment;
import com.example.demo.src.comment.entity.ReComment;
import com.example.demo.src.feed.entitiy.Feed;
import com.example.demo.src.feed.entitiy.FeedBookMark;
import com.example.demo.src.feed.entitiy.FeedLike;
import com.example.demo.src.followMapping.entitiy.FollowMapping;
import com.example.demo.src.user.enumeration.Role;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private LocalDateTime lastLoginTime;

    @Enumerated(EnumType.STRING)
    private Role role;


    @OneToMany(mappedBy = "user")
    List<ChattingText> chattingTextList = new ArrayList<>();


    @OneToMany(mappedBy = "user")
    List<Feed> feedList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<FeedBookMark> feedBookMarkList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<FeedComment> feedCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<FeedLike> feedLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<ReComment> reCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<CommentLike> commentLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "followUser") //팔로우 당하는 유저
    List<FollowMapping> followMappingList = new ArrayList<>();

    @OneToMany(mappedBy = "followerUser") //팔로우 하는 유저(팔로워가 됨)
    List<FollowMapping> followerMappingList = new ArrayList<>();


    @OneToMany(mappedBy = "sendUser")
    List<ChattingRoom> sendUserList = new ArrayList<>();

    @OneToMany(mappedBy = "receiveUser")
    List<ChattingRoom> receiveUserList = new ArrayList<>();



    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
    public User(String email, String password, String name,Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role=role;
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
