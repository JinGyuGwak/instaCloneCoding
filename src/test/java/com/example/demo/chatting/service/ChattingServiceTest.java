package com.example.demo.chatting.service;

import com.example.demo.src.chatting.dto.ChattingDto;
import com.example.demo.src.chatting.entitiy.ChattingRoom;
import com.example.demo.src.chatting.repository.ChattingRoomRepository;
import com.example.demo.src.chatting.repository.ChattingTextRepository;
import com.example.demo.src.chatting.service.ChattingService;
import com.example.demo.src.common.entity.BaseEntity;
import com.example.demo.src.user.entitiy.User;
import com.example.demo.src.user.repository.UserRepository;
import com.example.demo.src.util.FuncUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ChattingServiceTest {
    @Autowired
    private FuncUser funcUser;
    @Autowired
    private ChattingRoomRepository chattingRoomRepository;
    @Autowired
    private ChattingTextRepository chattingTextRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChattingService chattingService;

    User receiveUser(){
        return User.builder()
                .email("test1@naver.com")
                .name("고마츠나나")
                .password("269599zz")
                .build();
    }
    User sendUser(){
        return User.builder()
                .email("test2@naver.com")
                .name("카리나")
                .password("269599zz")
                .build();
    }
    @BeforeEach
    public void be(){
        userRepository.save(receiveUser());
        userRepository.save(sendUser());

    }
    @Test
    public void createChatting_test(){
        User receiveUser = userRepository.findByEmail("test1@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없어용"));
        User sendUser = userRepository.findByEmail("test2@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없어용"));
        ChattingDto.ChattingRoomDto chatting = chattingService.createChatting(
                ChattingDto.ChattingRoomDto.builder()
                        .receiveUserId(receiveUser.getId())
                        .sendUserId(sendUser.getId())
                        .build());
        assertThat(chatting.getSendUserId()).isEqualTo(sendUser.getId());
        assertThat(chatting.getReceiveUserId()).isEqualTo(receiveUser.getId());
    }
    @Test
    public void getChattingRoom_test(){
        User receiveUser = userRepository.findByEmail("test1@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없어용"));
        User sendUser = userRepository.findByEmail("test2@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없어용"));
        ChattingDto.ChattingRoomDto chatting = chattingService.createChatting(
                ChattingDto.ChattingRoomDto.builder()
                        .receiveUserId(receiveUser.getId())
                        .sendUserId(sendUser.getId())
                        .build());

        List<ChattingDto.ChattingRoomDto> chattingRoom = chattingService.getChattingRoom(sendUser.getId());
        assertThat(chattingRoom.size()).isEqualTo(1);
        assertThat(chattingRoom.get(0).getSendUserId()).isEqualTo(sendUser.getId());
    }
    @Test
    public void deleteChatting_test(){
        User receiveUser = userRepository.findByEmail("test1@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없어용"));
        User sendUser = userRepository.findByEmail("test2@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없어용"));
        ChattingDto.ChattingRoomDto chatting = chattingService.createChatting(
                ChattingDto.ChattingRoomDto.builder()
                        .receiveUserId(receiveUser.getId())
                        .sendUserId(sendUser.getId())
                        .build());
        chattingService.deleteChatting(chatting.getChattingRoomId());
        assertThrows(IllegalArgumentException.class, () -> {
            chattingRoomRepository
                    .findByIdAndState(chatting.getChattingRoomId(), BaseEntity.State.ACTIVE)
                    .orElseThrow(()->new IllegalArgumentException("카리나는 진짜 왜이렇게 이쁜걸까"));
        });
    }
    @Test
    public void sendMessage_test(){
        User receiveUser = userRepository.findByEmail("test1@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없어용"));
        User sendUser = userRepository.findByEmail("test2@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없어용"));
        ChattingDto.ChattingRoomDto chatting = chattingService.createChatting(
                ChattingDto.ChattingRoomDto.builder()
                        .receiveUserId(receiveUser.getId())
                        .sendUserId(sendUser.getId())
                        .build());

        ChattingDto.MessageResponseDto message = chattingService.sendMessage(chatting.getChattingRoomId()
                , ChattingDto.MessageDto.builder()
                        .sendUserId(sendUser.getId())
                        .chatText("카리나는 신이 아닐까?")
                        .build());
        assertThat(message.getChattingRoomId()).isEqualTo(chatting.getChattingRoomId());
        assertThat(message.getChatText()).isEqualTo("카리나는 신이 아닐까?");
        assertThat(message.getSendUserId()).isEqualTo(sendUser.getId());
    }
    @Test
    public void getMessage_test(){
        User receiveUser = userRepository.findByEmail("test1@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없어용"));
        User sendUser = userRepository.findByEmail("test2@naver.com")
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없어용"));
        ChattingDto.ChattingRoomDto chatting = chattingService.createChatting(
                ChattingDto.ChattingRoomDto.builder()
                        .receiveUserId(receiveUser.getId())
                        .sendUserId(sendUser.getId())
                        .build());

        chattingService.sendMessage(chatting.getChattingRoomId()
                , ChattingDto.MessageDto.builder()
                        .sendUserId(sendUser.getId())
                        .chatText("카리나는 신이 맞아")
                        .build());

        List<ChattingDto.MessageResponseDto> message =
                chattingService.getMessage(chatting.getChattingRoomId());

        assertThat(message.size()).isEqualTo(1);
        assertThat(message.get(0).getChattingRoomId()).isEqualTo(chatting.getChattingRoomId());
        assertThat(message.get(0).getChatText()).isEqualTo("카리나는 신이 맞아");
        assertThat(message.get(0).getSendUserId()).isEqualTo(sendUser.getId());


    }





}
