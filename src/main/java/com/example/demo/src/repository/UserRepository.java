package com.example.demo.src.repository;

import com.example.demo.src.entity.Feed;
import com.example.demo.src.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("select u from User u where (:email is null or u.email = :email) and " +
            "(:name is null or u.name = :name) and " +
            "(:fromDate is null or u.createdAt >= :fromDate) and " +
            "(:toDate is null or u.createdAt <= :toDate) and " +
            "(:state is null or u.state = :state)")
    List<User> findByCriteria(
            @Param("email") String email,
            @Param("name") String name,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            @Param("state") State state,
            Pageable pageable);



    //어드민 페이지
    Page<User> findByState(State state, Pageable pageable);
    Page<User> findAllByName(String name, Pageable pageable);
    Page<User> findAllByEmail(String email, Pageable pageable);
    Page<User> findAllByEmailAndName(String email,String name,Pageable pageable);
    Page<User> findAllByCreatedAt(LocalDateTime created, Pageable pageable);


}
