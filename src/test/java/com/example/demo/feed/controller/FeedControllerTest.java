package com.example.demo.feed.controller;


import com.example.demo.src.feed.controller.FeedController;
import com.example.demo.src.feed.dto.FeedDto;
import com.example.demo.src.feed.service.FeedService;
import com.example.demo.src.myPage.service.MyPageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FeedController.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class FeedControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMApper;
    @MockBean
    private FeedService feedService;

    private static MockMultipartFile getMockMultipart() throws IOException{
        InputStream inputStream = new ClassPathResource("").getInputStream();
        MockMultipartFile file = new MockMultipartFile("files", "saveOriginalName.png",
                "image/png", inputStream);
        return file;
    }
    private static MockMultipartHttpServletRequestBuilder getPutMultipart(String uri) {
        MockMultipartHttpServletRequestBuilder multipartBuilder =
                MockMvcRequestBuilders.multipart(uri);
        multipartBuilder.with(request -> {
            request.setMethod("POST");
            return request;
        });
        return multipartBuilder;
    }
    FeedDto.PostFeedRes dummyPostFeedRes(){
        return FeedDto.PostFeedRes.builder()
                .feedId(1L)
                .userId(3L)
                .build();
    }
    FeedDto.UpdateFeedDto dummyUpdateFeedDto(){
        return FeedDto.UpdateFeedDto.builder()
                .updateText("업데이트 테스트")
                .feedId(1L)
                .build();
    }
    FeedDto.GetFeedRes dummyGetFeedRes(){
        FeedDto.GetFeedRes build = FeedDto.GetFeedRes.builder()
                .feedId(1L)
                .username("카리나는 신이야")
                .feedText("테스트 본문 내용")
                .build();
        build.getFeedContentListURL().add("이미지 1 URL");
        build.getFeedContentListURL().add("이미지 2 URL");
        return build;
    }

    @Test
    @WithMockUser
    public void createFeed_test() throws Exception{
        MockMultipartFile file = getMockMultipart();
        given(feedService.upload(anyLong(),any(),anyString())).willReturn(dummyPostFeedRes());
        //when & then
        MockMultipartHttpServletRequestBuilder multipart = getPutMultipart("/feed");

        mockMvc.perform(multipart
                .file(file)
                .param("userId","1")
                .param("postText","Feed Test")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("feed/createFeed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestParameters(
                                parameterWithName("userId").description("피드를 등록할 UserId(로그인 한 유저)"),
                                parameterWithName("postText").optional().description("피드 본문 내용")
                        ),
                        relaxedRequestParts(
                                partWithName("files").description("첨부 파일 리스트")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("feedId").description("생성한 피드 Id").type(JsonFieldType.NUMBER),
                                fieldWithPath("userId").description("유저 ID").type(JsonFieldType.NUMBER)
                        )
                ));
    }
    @Test
    @WithMockUser
    public void deleteFeed_test() throws Exception{
        //given
        doNothing().when(feedService).deleteFeed(anyLong());
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/feed/{feedId}",1L)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("feed/deleteFeed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("feedId").description("삭제할 FeedId")
                        ),
                        responseBody()
                        ));
    }
    @Test
    @WithMockUser
    public void updateFeed_test() throws Exception{
        given(feedService.updateFeed(any())).willReturn(dummyUpdateFeedDto());
        //when & then
        mockMvc.perform(patch("/feed")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMApper.writeValueAsString(dummyUpdateFeedDto()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(document("feed/updateFeedText",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("updateText").description("수정 할 본문 내용").type(JsonFieldType.STRING),
                                fieldWithPath("feedId").description("수정 할 피드").type(JsonFieldType.NUMBER)
                        ),
                        relaxedResponseFields(
                                fieldWithPath("updateText").description("수정 할 본문 내용").type(JsonFieldType.STRING),
                                fieldWithPath("feedId").description("수정 할 피드").type(JsonFieldType.NUMBER)
                        )
                ));
    }
    @Test
    @WithMockUser
    public void searchAllFeed_test() throws Exception{
        List<FeedDto.GetFeedRes> list = new ArrayList<>();
        list.add(dummyGetFeedRes());
        given(feedService.searchAllFeed(any())).willReturn(list);

        //when & then
        mockMvc.perform(get("/feed")
                .param("page","0")
                .param("size","20"))
                .andExpect(status().isOk())
                .andDo(document("feed/getAllFeed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("조회할 페이지 (기본값 0)"),
                                parameterWithName("size").description("가져올 Item 수 (기본값 20)")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("[].feedId").description("FeedId"),
                                fieldWithPath("[].username").description("유저이름"),
                                fieldWithPath("[].feedText").description("Feed 본문"),
                                fieldWithPath("[].feedContentListURL[]").description("Feed 사진 URL")
                        )
                ));

    }
    @Test
    @WithMockUser
    public void searchUserFeed_test() throws Exception{
        List<FeedDto.GetFeedRes> list = new ArrayList<>();
        list.add(dummyGetFeedRes());
        given(feedService.searchUserFeed(anyLong())).willReturn(list);
        //when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/feed/{userId}",1L))
                .andExpect(status().isOk())
                .andDo(document("feed/getUserFeed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("userId").description("어떤 유저의 Feed를 볼것인가")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("[].feedId").description("FeedId"),
                                fieldWithPath("[].username").description("유저이름"),
                                fieldWithPath("[].feedText").description("Feed 본문"),
                                fieldWithPath("[].feedContentListURL[]").description("Feed 사진 URL")
                        )
                        ));
    }


}
