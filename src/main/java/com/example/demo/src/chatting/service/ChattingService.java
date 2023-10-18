package com.example.demo.src.chatting.service;


import com.example.demo.src.chatting.dto.ChattingDto;
import com.example.demo.src.chatting.dto.ChattingDto.ChattingRoomDto;
import com.example.demo.src.chatting.dto.ChattingDto.MessageDto;
import com.example.demo.src.chatting.dto.ChattingDto.MessageResponseDto;
import com.example.demo.src.chatting.repository.ChattingRoomRepository;
import com.example.demo.src.chatting.repository.ChattingTextRepository;
import com.example.demo.src.chatting.entitiy.ChattingRoom;
import com.example.demo.src.chatting.entitiy.ChattingText;
import com.example.demo.src.util.FuncUser;
import com.example.demo.src.user.entitiy.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.src.common.entity.BaseEntity.State.ACTIVE;

@RequiredArgsConstructor
@Service
public class ChattingService {
    private final FuncUser funcUser;
    private final ChattingRoomRepository chattingRoomRepository;
    private final ChattingTextRepository chattingTextRepository;

    //채팅방조회
    @Transactional(readOnly = true)
    public List<ChattingRoomDto> getChattingRoom(Long userId){
        if(chattingRoomRepository.findBySendUserIdOrReceiveUserId(userId,userId).isEmpty()){
            throw new IllegalArgumentException("채티방이 존재하지 않습니다.");
        }
        return chattingRoomRepository.findBySendUserIdOrReceiveUserId(userId, userId)
                .stream()
                .map(ChattingRoomDto::new)
                .collect(Collectors.toList());
    }
    //채팅생성
    @Transactional
    public ChattingRoomDto createChatting(ChattingRoomDto chattingRoomDto){
        User sendUser = funcUser.findUserByIdAndState(chattingRoomDto.getSendUserId());
        User receiveUser = funcUser.findUserByIdAndState(chattingRoomDto.getReceiveUserId());
        if(chattingRoomRepository.findBySendUserIdAndReceiveUserId(sendUser.getId(), receiveUser.getId()).isPresent() ||
                chattingRoomRepository.findBySendUserIdAndReceiveUserId(receiveUser.getId(),sendUser.getId()).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 채팅방입니다.");
        }
        ChattingRoom chattingRoom = new ChattingRoom(sendUser,receiveUser);
        chattingRoomRepository.save(chattingRoom);
        return new ChattingRoomDto(chattingRoom);
    }

    //채팅삭제
    @Transactional
    public void deleteChatting(Long chattingRoomId){
        ChattingRoom chattingRoom = chattingRoomRepository.findByIdAndState(chattingRoomId,ACTIVE)
                .orElseThrow(()-> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
        chattingRoom.deleteChattingRoom();
    }

    //채팅방에서 메시지 보내기
    @Transactional
    public MessageResponseDto sendMessage(Long chattingRoomId, MessageDto messageDto){
        ChattingRoom chattingRoom = chattingRoomRepository.findByIdAndState(chattingRoomId,ACTIVE)
                .orElseThrow(()-> new IllegalArgumentException("채팅방이 존재하지 않습니다."));
        User user = funcUser.findUserByIdAndState(messageDto.getSendUserId());
        ChattingText chattingText = new ChattingText(user,chattingRoom, messageDto.getChatText());
        chattingTextRepository.save(chattingText);
        return new MessageResponseDto(chattingText);
    }

    //특정 채팅방 메시지 조회
    @Transactional(readOnly = true)
    public List<MessageResponseDto> getMessage(Long chattingRoomId){
        return chattingTextRepository.findByChattingRoomIdAndState(chattingRoomId, ACTIVE)
                .stream()
                .map(MessageResponseDto::new)
                .collect(Collectors.toList());
    }

    //특정 채팅방 메시지 삭제
    @Transactional
    public void deleteMessage(Long chattingRoomId,Long chatTextId){
        ChattingText chattingText=
                chattingTextRepository.findByIdAndChattingRoomId(chatTextId,chattingRoomId)
                        .orElseThrow(()->new IllegalArgumentException("존재하지 않는 메시지 입니다."));
        chattingText.deleteMessage();
    }
}
