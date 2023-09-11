package com.minsproject.board.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minsproject.board.service.PostService;
import com.minsproject.board.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {

    private final MockMvc mvc;
//    private final FormDataEncoder formDataEncoder;
    @Autowired private ObjectMapper mapper;
    @MockBean private PostService postService;
    @MockBean private UserService userService;

    UserControllerTest(@Autowired MockMvc mvc) { this.mvc = mvc; }

    @DisplayName("로그인 페이지 - 정상")
    @Test
    void 로그인() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML));

        then(userService).shouldHaveNoInteractions();
        then(postService).shouldHaveNoInteractions();
    }

//    @DisplayName("회원 가입 - 정상")
//    @Test
//    public void 회원가입() throws Exception {
//        UserJoinRequest request = new UserJoinRequest("username", "password");
//
//
//
//    }
}
