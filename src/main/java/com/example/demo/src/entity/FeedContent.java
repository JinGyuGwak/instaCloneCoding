package com.example.demo.src.entity;

import com.example.demo.common.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "FEEDCONTENT") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class FeedContent extends BaseEntity {
    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedId")
    private Feed feed;

    @Column(nullable = false) //파일 이름
    private String postName;

    @Column(nullable = false) //파일 저장 경로
    private String postURL;

    private Long fileSize;


    public FeedContent(String postName, String postURL, Long fileSize){
        this.postName=postName;
        this.postURL=postURL;
        this.fileSize=fileSize;
    }

    //피드 정보 저장
    public void setFeed(Feed feed){
        this.feed=feed;

        if(!feed.getFeedContentList().contains(this)){
            //게시글에 현재 컨텐츠가 존재하지 않으면 컨텐츠 추가
            feed.getFeedContentList().add(this);
        }
    }



}
