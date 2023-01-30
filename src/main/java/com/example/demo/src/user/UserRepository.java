package com.example.demo.src.user;

import com.example.demo.src.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.demo.common.entity.BaseEntity.*;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByIdAndState(Long id, State state);
    Optional<User> findByEmailAndState(String email, State state);
    Optional<User> findByEmail(String email);


    List<User> findAllByEmailAndState(String email, State state);
    List<User> findAllByState(State state);



    //어드민 페이지
    Page<User> findByState(State state, Pageable pageable);
    Page<User> findAllByName(String name, Pageable pageable);
    Page<User> findAllByEmail(String email, Pageable pageable);
    Page<User> findAllByEmailAndName(String email,String name,Pageable pageable);
    Page<User> findAllByCreatedAt(LocalDateTime created, Pageable pageable);


}
