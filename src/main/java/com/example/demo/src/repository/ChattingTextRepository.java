package com.example.demo.src.repository;

import com.example.demo.src.entity.ChattingText;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import static com.example.demo.common.entity.BaseEntity.*;

public interface ChattingTextRepository extends JpaRepository<ChattingText,Long> {
    List<ChattingText> findByChattingRoomIdAndState(Long id, State state);
    Optional<ChattingText> findByIdAndChattingRoomId(Long chattingRoomId, Long id);
}
