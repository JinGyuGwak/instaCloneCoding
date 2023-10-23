package com.example.demo.src.user.repository;

import com.example.demo.src.common.entity.BaseEntity;
import com.example.demo.src.common.entity.BaseEntity.State;
import com.example.demo.src.user.entitiy.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByIdAndState(Long id, State state);
    Optional<User> findByEmailAndState(String email, State state);
    Optional<User> findByEmail(String email);

    List<User> findAllByEmailAndState(String email, State state);

    List<User> findAllByState(State state);



}
