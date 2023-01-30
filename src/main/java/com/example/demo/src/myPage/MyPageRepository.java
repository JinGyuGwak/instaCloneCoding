package com.example.demo.src.myPage;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.myPage.entity.MyPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static com.example.demo.common.entity.BaseEntity.*;

public interface MyPageRepository extends JpaRepository<MyPage,Long> {
    Optional<MyPage> findByUserIdAndState(Long userId, State state);
}
