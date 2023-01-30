package com.example.demo.src.chatting;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.chatting.entity.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import static com.example.demo.common.entity.BaseEntity.*;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom,Long> {
    Optional<ChattingRoom> findByIdAndState(Long id, State state);
    List<ChattingRoom> findBySendUserIdOrReceiveUserId(Long sendUserid,Long receiveUserid);


    Optional<ChattingRoom> findBySendUserIdAndReceiveUserId(Long sendUserid, Long receiveUserid);
}
