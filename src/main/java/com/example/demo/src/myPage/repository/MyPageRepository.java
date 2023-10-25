package com.example.demo.src.myPage.repository;

import com.example.demo.src.common.entity.BaseEntity;
import com.example.demo.src.common.entity.BaseEntity.State;
import com.example.demo.src.myPage.entitiy.MyPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyPageRepository extends JpaRepository<MyPage,Long> {
    Optional<MyPage> findByUserIdAndState(Long userId, State state);

    Optional<MyPage> findByName(String name);
}
