package com.example.demo.src.chatting.dto.response;

import com.example.demo.src.chatting.entitiy.ChattingRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChattingRoomRequestRes {
    private Long sendUserId;
    private Long receiveUserId;

    public ChattingRoomRequestRes(ChattingRoom chattingRoom){
        this.sendUserId=chattingRoom.getSendUser().getId();
        this.receiveUserId=chattingRoom.getReceiveUser().getId();
    }

}
