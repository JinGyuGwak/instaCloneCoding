package com.example.demo.src.entity;

import com.example.demo.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "CHATTINGTEXT") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class ChattingText extends BaseEntity {
    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chattingRoomId")
    private ChattingRoom chattingRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    private String chatText; //보낸 내역

    public ChattingText(User user, ChattingRoom chattingRoom, String chatText){
        this.user=user;
        this.chattingRoom=chattingRoom;
        this.chatText=chatText;
    }
    public void deleteMessage(){this.state=State.INACTIVE;}
}
