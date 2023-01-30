package com.example.demo.src.chatting.model;

import com.example.demo.src.chatting.entity.ChattingText;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MessageRequestDto {
    private Long userId;
    private String chatText;

    public MessageRequestDto(ChattingText chattingText){
        this.userId=chattingText.getUser().getId();
        this.chatText=chattingText.getChatText();
    }
}
