package com.example.demo.src.chatting.service;


import com.example.demo.src.common.exceptions.BaseException;
import com.example.demo.src.chatting.repository.ChattingRoomRepository;
import com.example.demo.src.chatting.repository.ChattingTextRepository;
import com.example.demo.src.chatting.entitiy.ChattingRoom;
import com.example.demo.src.chatting.entitiy.ChattingText;
import com.example.demo.src.chatting.dto.response.ChattingRoomRequestRes;
import com.example.demo.src.chatting.dto.response.MessageRequestRes;
import com.example.demo.src.func.FuncUser;
import com.example.demo.src.user.entitiy.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.src.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.src.common.response.BaseResponseStatus.*;

@Transactional
@RequiredArgsConstructor
@Service
public class ChattingService {
    private final FuncUser funcUser;
    private final ChattingRoomRepository chattingRoomRepository;
    private final ChattingTextRepository chattingTextRepository;

    //채팅방조회
    @Transactional
    public List<ChattingRoomRequestRes> getChattingRoom(Long userId){
        if(chattingRoomRepository.findBySendUserIdOrReceiveUserId(userId,userId).isEmpty()){
            throw new BaseException(NOT_FIND_CHATROOM);
        }
        return chattingRoomRepository.findBySendUserIdOrReceiveUserId(userId, userId)
                .stream()
                .map(ChattingRoomRequestRes::new)
                .collect(Collectors.toList());
    }

    //채팅생성
    @Transactional
    public ChattingRoomRequestRes createChatting(Long sendUserId, Long receiveUserId){
        User sendUser = funcUser.findUserByIdAndState(sendUserId);
        User receiveUser = funcUser.findUserByIdAndState(receiveUserId);
        if(chattingRoomRepository.findBySendUserIdAndReceiveUserId(sendUser.getId(), receiveUser.getId()).isPresent() ||
                chattingRoomRepository.findBySendUserIdAndReceiveUserId(receiveUser.getId(),sendUser.getId()).isPresent()){
            throw new BaseException(INVALID_CHATROOM);
        }
        ChattingRoom chattingRoom = new ChattingRoom(sendUser,receiveUser);
        chattingRoomRepository.save(chattingRoom);
        return new ChattingRoomRequestRes(chattingRoom);
    }

    //채팅삭제
    @Transactional
    public void deleteChatting(Long chattingRoomId){
        ChattingRoom chattingRoom = chattingRoomRepository.findByIdAndState(chattingRoomId,ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_CHATROOM));
        chattingRoom.deleteChattingRoom();
    }

    //채팅방에서 메시지 보내기
    @Transactional
    public MessageRequestRes sendMessage(Long chattingRoomId, Long userId, String charText){
        ChattingRoom chattingRoom = chattingRoomRepository.findByIdAndState(chattingRoomId,ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_CHATROOM));
        User user = funcUser.findUserByIdAndState(userId);
        ChattingText chattingText = new ChattingText(user,chattingRoom, charText);
        chattingTextRepository.save(chattingText);
        return new MessageRequestRes(chattingText);
    }

    //특정 채팅방 메시지 조회
    @Transactional
    public List<MessageRequestRes> getMessage(Long chattingRoomId){
        return chattingTextRepository.findByChattingRoomIdAndState(chattingRoomId, ACTIVE)
                .stream()
                .map(MessageRequestRes::new)
                .collect(Collectors.toList());
    }

    //특정 채팅방 메시지 삭제
    @Transactional
    public void deleteMessage(Long chattingRoomId,Long chatTextId){
        ChattingText chattingText=
                chattingTextRepository.findByIdAndChattingRoomId(chattingRoomId,chatTextId)
                        .orElseThrow(()->new BaseException(NOT_FIND_MESSAGE));
        chattingText.deleteMessage();
    }
}
