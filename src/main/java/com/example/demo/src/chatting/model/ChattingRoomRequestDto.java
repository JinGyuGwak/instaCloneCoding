package com.example.demo.src.chatting.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChattingRoomRequestDto {
    private Long sendUserId;
    private Long receiveUserId;

    public ChattingRoomRequestDto(Long sendUserId, Long receiveUserId){
        this.sendUserId=sendUserId;
        this.receiveUserId=receiveUserId;
    }
}
