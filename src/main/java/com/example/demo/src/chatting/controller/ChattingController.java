package com.example.demo.src.chatting.controller;


import com.example.demo.src.chatting.dto.ChattingDto;
import com.example.demo.src.chatting.dto.ChattingDto.*;
import com.example.demo.src.common.response.ResponseEntityCustom;
import com.example.demo.src.chatting.service.ChattingService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/chatting")
public class ChattingController {

    private final ChattingService chattingService;
    /**
     * 채팅방조회
     * @param userId
     * @return (내id, 상대id)
     */
    @GetMapping("{userId}")
    public ResponseEntity<List<ChattingRoomDto>> getChattingRoom(@PathVariable Long userId) {
        return new ResponseEntity<>(chattingService.getChattingRoom(userId), HttpStatus.OK);
    }
    /**
     * 채팅방 만들기
     * [POST] /chatting
     * @param chattingRoomRequestDto((sendUserId, receiveUserId)
     * @return
     */
    @PostMapping("")
    public ResponseEntity<ChattingRoomDto> createChatting(@RequestBody ChattingRoomDto chattingRoomRequestDto) {
        return new ResponseEntity<>(chattingService.createChatting(chattingRoomRequestDto), HttpStatus.OK);
    }
    /**
     * 채팅방 나가기
     * [DELETE] /chatting/{chattingRoomId}
     * @param chattingRoomId
     * @return1
     */
    @DeleteMapping("{chattingRoomId}")
    public ResponseEntity<String> deleteChatting(@PathVariable Long chattingRoomId) {
        chattingService.deleteChatting(chattingRoomId);
        String result = "채팅방을 나갑니다.";
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 채팅방에서 메시지 보내기
     * @param chattingRoomId
     * @param messageRequestDto(유저id, 메세지 내용)
     * @return
     */
    @PostMapping("/message/{chattingRoomId}")
    public ResponseEntity<MessageResponseDto> sendMessage(@PathVariable Long chattingRoomId,
                                                          @RequestBody MessageDto messageRequestDto) {
        return new ResponseEntity<>(chattingService.sendMessage(chattingRoomId, messageRequestDto), HttpStatus.OK);
    }

    /**
     * 채팅방 메시지 조회하기
     * @param chattingRoomId(채팅방id)
     * @return
     */
    @GetMapping("/message/{chattingRoomId}")
    public ResponseEntity<List<MessageResponseDto>> getMessage(@PathVariable Long chattingRoomId) {
        return new ResponseEntity<>(chattingService.getMessage(chattingRoomId), HttpStatus.OK);
    }

    /**
     * 채팅방 메시지 지우기
     * @param chattingRoomId 채팅방id
     * @param chatTextId 메시지id
     * @return
     */
    @DeleteMapping("/message/{chattingRoomId}/{chatTextId}")
    public ResponseEntity<String> getMessage(@PathVariable Long chattingRoomId,
                                             @PathVariable Long chatTextId) {
        chattingService.deleteMessage(chattingRoomId,chatTextId);
        String result = "삭제완료!";
        return new ResponseEntity<>(result, HttpStatus.OK);
    }



}
