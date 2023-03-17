package com.example.demo.src.repository;

import com.example.demo.src.entity.MyPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static com.example.demo.common.entity.BaseEntity.*;

public interface MyPageRepository extends JpaRepository<MyPage,Long> {
    Optional<MyPage> findByUserIdAndState(Long userId, State state);
}
