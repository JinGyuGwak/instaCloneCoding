package com.example.demo.src.chatting.repository;

import com.example.demo.src.chatting.entitiy.ChattingRoom;
import com.example.demo.src.common.entity.BaseEntity;
import com.example.demo.src.common.entity.BaseEntity.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom,Long> {
    Optional<ChattingRoom> findByIdAndState(Long id, State state);
    List<ChattingRoom> findBySendUserIdOrReceiveUserId(Long sendUserid,Long receiveUserid);


    Optional<ChattingRoom> findBySendUserIdAndReceiveUserId(Long sendUserid, Long receiveUserid);
}
