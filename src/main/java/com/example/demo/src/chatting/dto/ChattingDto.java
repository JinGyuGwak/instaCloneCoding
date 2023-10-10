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
    public static class MessageDto extends ChattingDto{
        protected String chatText;
    }
    @Getter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageResponseDto extends MessageDto{
        private String chatText;
        private Long chattingRoomId;
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
    public static class ChattingRoomDto extends ChattingDto{
        private Long receiveUserId;

        public ChattingRoomDto(ChattingRoom chattingRoom){
            this.sendUserId=chattingRoom.getSendUser().getId();
            this.receiveUserId=chattingRoom.getReceiveUser().getId();
        }
    }
}
