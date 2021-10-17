package com.example;

import com.example.controller.MainController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("user")
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql", "/messagesList-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/messagesList-after.sql", "/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MainController controller;

    @Test
    public void mainPage() throws Exception{
        mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='navbarSupportedContent']/div").string("user"));
    }

    @Test
    public void messageList() throws Exception{
        mockMvc.perform(get("/main"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='messageList']/div").nodeCount(4));
    }

    @Test
    public void filterTest() throws Exception{
        mockMvc.perform(get("/main").param("filter", "filter test"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='messageList']/div").nodeCount(1))
                .andExpect(xpath("//div[@id='messageList']/div[@data-id='4']").exists());
    }

    @Test
    public void addMessageTest() throws Exception{
        MockHttpServletRequestBuilder multipart = multipart("/main")
                .file("file", "854638910".getBytes())
                .param("text", "adding file to post")
                .param("tag", "file test")
                .with(csrf());

        mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='messageList']/div").nodeCount(5))
                .andExpect(xpath("//div[@id='messageList']/div[@data-id='10']").exists())
                .andExpect(xpath("//div[@id='messageList']/div[@data-id='10']/div/span")
                        .string("adding file to post"))
                .andExpect(xpath("//div[@id='messageList']/div[@data-id='10']/div/i")
                .string("#file test"));
    }
}
