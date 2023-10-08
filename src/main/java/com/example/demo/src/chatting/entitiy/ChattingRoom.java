package com.example.demo.src.chatting.entitiy;

import com.example.demo.src.common.entity.BaseEntity;
import com.example.demo.src.user.entitiy.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.src.common.entity.BaseEntity.State.INACTIVE;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "CHATTINGROOM") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class ChattingRoom extends BaseEntity {
    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sendUserId")
    private User sendUser; //보낸 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiveUserId")
    private User receiveUser;//받는 유저

    @OneToMany(mappedBy = "chattingRoom")
    List<ChattingText> chattingTextList = new ArrayList<>();

    public ChattingRoom(User sendUser, User receiveUser){
        this.sendUser=sendUser;
        this.receiveUser=receiveUser;
    }
    public void deleteChattingRoom(){this.state= INACTIVE;}

}
