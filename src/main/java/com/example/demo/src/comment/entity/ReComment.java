package com.example.demo.src.comment.entity;

import com.example.demo.src.common.entity.BaseEntity;
import com.example.demo.src.user.entitiy.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.example.demo.src.common.entity.BaseEntity.State.INACTIVE;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "RECOMMENT") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class ReComment extends BaseEntity {
    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedCommentId")
    private FeedComment feedComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Column(nullable = false, length = 2000)
    private String reCommentText;

    @Builder
    public ReComment(FeedComment feedComment, User user, String reCommentText){
        this.feedComment=feedComment;
        this.user=user;
        this.reCommentText=reCommentText;
    }
    public void updateReCommentText(String reCommentText){
        this.reCommentText=reCommentText;
    }
    public void deleteComment(){
        this.state= INACTIVE;
    }
}
