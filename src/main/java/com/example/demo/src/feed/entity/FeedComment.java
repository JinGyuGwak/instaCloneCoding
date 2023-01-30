package com.example.demo.src.feed.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.common.entity.BaseEntity.State.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "FEEDCOMMENT") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class FeedComment extends BaseEntity {
    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "feedId")
    private Feed feed;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(nullable = false, length = 2000)
    private String commentText;

    @OneToMany(mappedBy = "feedComment", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    List<CommentLike> commentLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "feedComment", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    List<ReComment> reCommentList = new ArrayList<>();

    @Builder
    public FeedComment(Feed feed, User user, String commentText){
        this.feed=feed;
        this.user=user;
        this.commentText=commentText;
    }

    public void updateFeedComment(String commentText){
        this.commentText=commentText;
    }
    public void deleteComment(){
        this.state= INACTIVE;

    }

}
