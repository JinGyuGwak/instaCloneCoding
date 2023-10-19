package com.example.demo.feed;


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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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

    @Test
    @WithMockUser
    public void createFeed_test() throws Exception{
        MockMultipartFile file = getMockMultipart();
//        List<MockMultipartFile> fileList = new ArrayList<>();
//        fileList.add(file);
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
                .andDo(document("createFeed",
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
}
