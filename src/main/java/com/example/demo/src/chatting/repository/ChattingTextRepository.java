package com.example.demo.src.chatting.repository;

import com.example.demo.src.chatting.entitiy.ChattingText;
import com.example.demo.src.common.entity.BaseEntity;
import com.example.demo.src.common.entity.BaseEntity.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChattingTextRepository extends JpaRepository<ChattingText,Long> {
    List<ChattingText> findByChattingRoomIdAndState(Long id, State state);
    Optional<ChattingText> findByIdAndChattingRoomId(Long id, Long chattingRoomId);

}
