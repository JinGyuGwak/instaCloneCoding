package com.example.demo.src.admin.entitiy;

import com.example.demo.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "LOGENTITY") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class LogEntity extends BaseEntity {
    @Id // PK를 의미하는 어노테이션
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Enumerated(EnumType.STRING)
    private Domain domain; //회원, 피드, 신고, 댓글 등

    private String work;

    public LogEntity(String email, Domain domain, String work){
        this.email=email;
        this.domain=domain;
        this.work=work;
    }
    public enum Domain {
        USER, FEED, REPORT, COMMENT;
    }


}
