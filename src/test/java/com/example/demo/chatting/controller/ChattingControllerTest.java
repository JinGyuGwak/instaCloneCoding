package com.example.demo.chatting.controller;


import com.example.demo.src.chatting.controller.ChattingController;
import com.example.demo.src.chatting.dto.ChattingDto;
import com.example.demo.src.chatting.service.ChattingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(ChattingController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class ChattingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ChattingService chattingService;

    ChattingDto.ChattingRoomDto dummyChattingRoomDto1(){
        return ChattingDto.ChattingRoomDto.builder()
                .chattingRoomId(1L)
                .receiveUserId(1L)
                .sendUserId(2L)
                .build();
    }
    ChattingDto.ChattingRoomDto dummyChattingRoomDto2(){
        return ChattingDto.ChattingRoomDto.builder()
                .chattingRoomId(2L)
                .receiveUserId(1L)
                .sendUserId(3L)
                .build();
    }
    ChattingDto.MessageResponseDto dummyMessageResponseDto1(){
        return ChattingDto.MessageResponseDto.builder()
                .chatText("카리나는신이야")
                .chattingRoomId(1L)
                .sendUserId(2L)
                .build();
    }
    ChattingDto.MessageResponseDto dummyMessageResponseDto2(){
        return ChattingDto.MessageResponseDto.builder()
                .chatText("나도 그렇게 생각해")
                .chattingRoomId(1L)
                .sendUserId(1L)
                .build();
    }
    ChattingDto.MessageDto dummyMessageDto(){
        return ChattingDto.MessageDto.builder()
                .sendUserId(2L)
                .chatText("카리나는신이야")
                .build();
    }

    @Test
    @WithMockUser
    public void getChattingRoom_test() throws Exception{
        //given
        List<ChattingDto.ChattingRoomDto> list = new ArrayList<>();
        list.add(dummyChattingRoomDto1());
        list.add(dummyChattingRoomDto2());
        given(chattingService.getChattingRoom(anyLong())).willReturn(list);
        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/chatting/{userId}",1L))
                .andExpect(status().isOk())
                .andDo(document("getChattingRoom",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("어떤 유저의 채팅방을 조회할것인가")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("[].chattingRoomId").description("채팅방 Id"),
                                fieldWithPath("[].receiveUserId").description("처음 메시지 받은 UserId"),
                                fieldWithPath("[].sendUserId").description("처음 메시지 보낸 UserId")
                        )
                ));
    }
    @Test
    @WithMockUser
    public void createChatting_test() throws Exception{
        given(chattingService.createChatting(any())).willReturn(dummyChattingRoomDto1());
        //when then
        mockMvc.perform(post("/chatting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyChattingRoomDto1()))
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("createChatting",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("receiveUserId").description("처음 메시지 받은 UserId"),
                                fieldWithPath("sendUserId").description("처음 메시지 보낸 UserId")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("receiveUserId").description("처음 메시지 받은 UserId"),
                                fieldWithPath("sendUserId").description("처음 메시지 보낸 UserId")
                        )
                ));
    }
    @Test
    @WithMockUser
    public void deleteChatting_test() throws Exception{
        //given
        doNothing().when(chattingService).deleteChatting(anyLong());
        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/chatting/{chattingRoomId}",1L)
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("deleteChattingRoom",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("chattingRoomId").description("나갈 채팅방 Id")
                        ),
                        responseBody()
                        ));
    }
    @Test
    @WithMockUser
    public void sendMessage_test() throws Exception{
        given(chattingService.sendMessage(anyLong(),any())).willReturn(dummyMessageResponseDto1());
        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/chatting/message/{chattingRoomId}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dummyMessageDto()))
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("sendMessage",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("chattingRoomId").description("메시지 보낼 채팅방 Id")
                        ),
                        relaxedRequestFields(
                                fieldWithPath("sendUserId").description("메시지 보내는 UserId"),
                                fieldWithPath("chatText").description("메시지 내용")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("chattingRoomId").description("채팅방 Id"),
                                fieldWithPath("sendUserId").description("메시지 보내는 UserId"),
                                fieldWithPath("chatText").description("메시지 내용")
                        )
                ));
    }
    @Test
    @WithMockUser
    public void getMessage_test() throws Exception{
        //given
        List<ChattingDto.MessageResponseDto> list = new ArrayList<>();
        list.add(dummyMessageResponseDto1());
        list.add(dummyMessageResponseDto2());
        given(chattingService.getMessage(anyLong())).willReturn(list);
        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/chatting/message/{chattingRoomId}",1L))
                .andExpect(status().isOk())
                .andDo(document("getMessage",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("chattingRoomId").description("메시지 조회할 채팅방 Id")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("[].chattingRoomId").description("채팅방 Id"),
                                fieldWithPath("[].sendUserId").description("메시지 보내는 UserId"),
                                fieldWithPath("[].chatText").description("메시지 내용")
                        )

                ));
    }
    @Test
    @WithMockUser
    public void deleteMessage_test() throws Exception{
        //given
        doNothing().when(chattingService).deleteMessage(anyLong(),anyLong());
        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/chatting/message/{chattingRoomId}/{chatTextId}",1L,1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("deleteMessage",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("chattingRoomId").description("삭제할 메시지가 있는 채팅방 Id"),
                                parameterWithName("chatTextId").description("삭제할 메시지 Id")
                        ),
                        responseBody()

                ));
    }

}
