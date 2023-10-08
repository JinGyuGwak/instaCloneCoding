package com.example.demo.src.chatting.controller;


import com.example.demo.src.common.response.ResponseEntityCustom;
import com.example.demo.src.chatting.service.ChattingService;
import com.example.demo.src.chatting.dto.response.ChattingRoomRequestRes;
import com.example.demo.src.chatting.dto.response.MessageRequestRes;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @ResponseBody
    @GetMapping("{userId}")
    public ResponseEntityCustom<List<ChattingRoomRequestRes>> getChattingRoom(@PathVariable Long userId) {
        List<ChattingRoomRequestRes> chattingRoomRequestResList = chattingService.getChattingRoom(userId);
        return new ResponseEntityCustom<>(chattingRoomRequestResList);
    }
    /**
     * 채팅방 만들기
     * [POST] /chatting
     * @param chattingRoomRequestDto((sendUserId, receiveUserId)
     * @return
     */
    @ResponseBody
    @PostMapping("")
    public ResponseEntityCustom<ChattingRoomRequestRes> createChatting(@RequestBody ChattingRoomRequestDto chattingRoomRequestDto) {

        ChattingRoomRequestRes chattingRoomRequestRes = chattingService.createChatting(
                chattingRoomRequestDto.getSendUserId(), chattingRoomRequestDto.getReceiveUserId());
        return new ResponseEntityCustom<>(chattingRoomRequestRes);
    }
    /**
     * 채팅방 나가기
     * [DELETE] /chatting/{chattingRoomId}
     * @param chattingRoomId
     * @return
     */
    @ResponseBody
    @DeleteMapping("{chattingRoomId}")
    public ResponseEntityCustom<String> deleteChatting(@PathVariable Long chattingRoomId) {
        chattingService.deleteChatting(chattingRoomId);
        String result = "채팅방을 나갑니다.";
        return new ResponseEntityCustom<>(result);
    }

    /**
     * 채팅방에서 메시지 보내기
     * @param chattingRoomId
     * @param messageRequestDto(유저id, 메세지 내용)
     * @return
     */
    @ResponseBody
    @PostMapping("/message/{chattingRoomId}")
    public ResponseEntityCustom<MessageRequestRes> sendMessage(@PathVariable Long chattingRoomId,
                                                               @RequestBody MessageRequestDto messageRequestDto) {
        MessageRequestRes messageRequestRes = chattingService.sendMessage(
                chattingRoomId, messageRequestDto.getUserId(), messageRequestDto.getChatText());
        return new ResponseEntityCustom<>(messageRequestRes);
    }

    /**
     * 채팅방 메시지 조회하기
     * @param chattingRoomId(채팅방id)
     * @return
     */
    @ResponseBody
    @GetMapping("/message/{chattingRoomId}")
    public ResponseEntityCustom<List<MessageRequestRes>> getMessage(@PathVariable Long chattingRoomId) {
        List<MessageRequestRes> messageRequestResList =
                chattingService.getMessage(chattingRoomId);
        return new ResponseEntityCustom<>(messageRequestResList);
    }

    /**
     * 채팅방 메시지 지우기
     * @param chattingRoomId 채팅방id
     * @param chatTextId 메시지id
     * @return
     */
    @ResponseBody
    @DeleteMapping("/message/{chattingRoomId}/{chatTextId}")
    public ResponseEntityCustom<String> getMessage(@PathVariable Long chattingRoomId,
                                                   @PathVariable Long chatTextId) {
        chattingService.deleteMessage(chattingRoomId,chatTextId);
        String result = "삭제완료!";
        return new ResponseEntityCustom<>(result);
    }

    @Getter
    private static class ChattingRoomRequestDto{
        private Long sendUserId;
        private Long receiveUserId;
    }

    @Getter
    private static class MessageRequestDto{
        private Long userId;
        private String chatText;
    }




}
