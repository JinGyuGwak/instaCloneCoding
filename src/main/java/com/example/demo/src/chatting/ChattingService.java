package com.example.demo.src.chatting;


import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.chatting.entity.ChattingRoom;
import com.example.demo.src.chatting.entity.ChattingText;
import com.example.demo.src.chatting.model.ChattingRoomRequestRes;
import com.example.demo.src.chatting.model.ChattingRoomRequestDto;
import com.example.demo.src.chatting.model.MessageRequestDto;
import com.example.demo.src.chatting.model.MessageRequestRes;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.*;

@Transactional
@RequiredArgsConstructor
@Service
public class ChattingService {
    private final UserRepository userRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final ChattingTextRepository chattingTextRepository;

    //채팅방조회
    public List<ChattingRoomRequestRes> getChattingRoom(Long userId){
        if(chattingRoomRepository.findBySendUserIdOrReceiveUserId(userId,userId).isEmpty()){
            throw new BaseException(NOT_FIND_CHATROOM);
        }
        List<ChattingRoom> chattingRoomList = chattingRoomRepository.findBySendUserIdOrReceiveUserId(userId,userId);
        List<ChattingRoomRequestRes> chattingRoomRequestResList = new ArrayList<>();
        for(ChattingRoom chattingRoom : chattingRoomList){
            ChattingRoomRequestRes a = new ChattingRoomRequestRes(chattingRoom);
            chattingRoomRequestResList.add(a);
        }
        return chattingRoomRequestResList;

    }

    //채팅생성
    public ChattingRoomRequestRes createChatting(ChattingRoomRequestDto chattingRoomRequestDto){
        User sendUser = userRepository.findByIdAndState(chattingRoomRequestDto.getSendUserId(), ACTIVE)
                .orElseThrow(()->new BaseException(NOT_FIND_USER));
        User receiveUser = userRepository.findByIdAndState(chattingRoomRequestDto.getReceiveUserId(), ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_USER));
        if(chattingRoomRepository.findBySendUserIdAndReceiveUserId(sendUser.getId(), receiveUser.getId()).isPresent() ||
                chattingRoomRepository.findBySendUserIdAndReceiveUserId(receiveUser.getId(),sendUser.getId()).isPresent()){
            throw new BaseException(INVALID_CHATROOM);
        }

        ChattingRoom chattingRoom = new ChattingRoom(sendUser,receiveUser);
        chattingRoomRepository.save(chattingRoom);
        return new ChattingRoomRequestRes(chattingRoom);
    }
    //채팅삭제
    public void deleteChatting(Long chattingRoomId){
        ChattingRoom chattingRoom = chattingRoomRepository.findByIdAndState(chattingRoomId,ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_CHATROOM));
        chattingRoom.deleteChattingRoom();
    }
    //채팅방에서 메시지 보내기
    public MessageRequestRes sendMessage(Long chattingRoomId, MessageRequestDto messageRequestDto){
        ChattingRoom chattingRoom = chattingRoomRepository.findByIdAndState(chattingRoomId,ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_CHATROOM));
        User user = userRepository.findByIdAndState(messageRequestDto.getUserId(),ACTIVE)
                .orElseThrow(()-> new BaseException(NOT_FIND_USER));
        ChattingText chattingText = new ChattingText(user,chattingRoom, messageRequestDto.getChatText());
        chattingTextRepository.save(chattingText);
        return new MessageRequestRes(chattingText);
    }
    //특정 채팅방 메시지 조회
    public List<MessageRequestRes> getMessage(Long chattingRoomId){
        List<ChattingText> chattingTextList = chattingTextRepository.
                findByChattingRoomIdAndState(chattingRoomId,ACTIVE);
        List<MessageRequestRes> messageRequestResList = new ArrayList<>();
        for(ChattingText chattingText : chattingTextList){
            MessageRequestRes a = new MessageRequestRes(chattingText);
            messageRequestResList.add(a);
        }
        return messageRequestResList;
    }
    //특정 채팅방 메시지 삭제
    public void deleteMessage(Long chattingRoomId,Long chatTextId){
        ChattingText chattingText=
                chattingTextRepository.findByIdAndChattingRoomId(chattingRoomId,chatTextId)
                        .orElseThrow(()->new BaseException(NOT_FIND_MESSAGE));
        chattingText.deleteMessage();

    }

}
