package com.example.demo.src.chatting.dto;

import com.example.demo.src.chatting.entitiy.ChattingRoom;
import com.example.demo.src.chatting.entitiy.ChattingText;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ChattingDto {
    protected Long sendUserId;

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageDto {
        private Long sendUserId;
        private String chatText;
    }
    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageResponseDto {
        private String chatText;
        private Long chattingRoomId;
        private Long sendUserId;
        public MessageResponseDto(ChattingText chattingText){
            this.chattingRoomId=chattingText.getChattingRoom().getId();
            this.sendUserId=chattingText.getUser().getId();
            this.chatText=chattingText.getChatText();
        }
    }

    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChattingRoomDto{
        private Long chattingRoomId;
        private Long receiveUserId;
        private Long sendUserId;

        public ChattingRoomDto(ChattingRoom chattingRoom){
            this.chattingRoomId=chattingRoom.getId();
            this.sendUserId=chattingRoom.getSendUser().getId();
            this.receiveUserId=chattingRoom.getReceiveUser().getId();
        }
    }
}
