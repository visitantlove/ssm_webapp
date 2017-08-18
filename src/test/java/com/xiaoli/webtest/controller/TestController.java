package com.xiaoli.webtest.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:conf/spring-mvc.xml", "classpath*:conf/spring-mybatis.xml"})
public class TestController {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.wac).build();
    }

    @Test
    public void testUserControllerShowAll() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/user/showAll.do");
        mockMvc.perform(mockHttpServletRequestBuilder).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testUserControllerDemo() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/user/demo.do");
        mockHttpServletRequestBuilder.param("username", "zhangsan");
        mockHttpServletRequestBuilder.param("age", "22");
        mockHttpServletRequestBuilder.param("sex", "男");
        mockMvc.perform(mockHttpServletRequestBuilder).andExpect(status().isOk()).andDo(print());
    }

}
