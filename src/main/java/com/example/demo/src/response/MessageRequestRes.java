package com.example.demo.src.response;

import com.example.demo.src.entity.ChattingText;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageRequestRes {
    private Long chattingRoomId;
    private Long userId;
    private String chatText;

    public MessageRequestRes(ChattingText chattingText){
        this.chattingRoomId=chattingText.getChattingRoom().getId();
        this.userId=chattingText.getUser().getId();
        this.chatText=chattingText.getChatText();
    }


}
