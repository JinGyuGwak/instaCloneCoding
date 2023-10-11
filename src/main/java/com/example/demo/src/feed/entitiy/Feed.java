package com.example.demo.src.feed.entitiy;


import com.example.demo.src.common.entity.BaseEntity;
import com.example.demo.src.comment.entity.FeedComment;
import com.example.demo.src.user.entitiy.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "FEED") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class Feed extends BaseEntity {
    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Column(nullable = false, length = 2200)
    private String postText;

    private int reportCount;


    @Builder.Default
    @OneToMany(mappedBy = "feed",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    List<FeedContent> feedContentList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE)
    List<FeedBookMark> feedBookMarkList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE)
    List<FeedComment> feedCommentList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE)
    List<FeedLike> feedLikeList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE)
    List<FeedReport> feedReportList = new ArrayList<>();

    //피드에서 파일 처리 위함
    public void addFeedContent(FeedContent feedContent){
        this.feedContentList.add(feedContent);
        //피드에 파일이 저장되어있지 않은 경우 -> 파일 저장
        if(feedContent.getFeed() != this) feedContent.setFeed(this);
    }
    public void reported(){this.reportCount+=1;}

    //게시글 내용 변경
    public void update(String postText){ this.postText=postText; }

    public void deleteFeed(){
        this.state=State.INACTIVE;
    }

}
