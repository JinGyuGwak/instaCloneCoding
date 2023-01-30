package com.example.demo.src.feed.entity;

import com.example.demo.common.entity.BaseEntity;
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
@Table(name = "COMMENTLIKE") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class CommentLike extends BaseEntity {
    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "feedCommentId")
    private FeedComment feedComment;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public CommentLike(User user, FeedComment feedComment){
        this.user=user;
        this.feedComment=feedComment;
    }

}
