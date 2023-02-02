package cn.zefre.base.web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.annotation.Resource;

/**
 * @author pujian
 * @date 2023/2/2 14:23
 */
@WebMvcTest(controllers = UserController.class)
public class WrapResponseTest {

    @Resource
    private MockMvc mockMvc;

    @Configuration
    @ComponentScan
    static class Config {

    }

    @Test
    public void testVoid() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/base/user/void");
        mockMvc.perform(requestBuilder);
    }

    @Test
    public void testBoolean() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/base/user/getBoolean");
        String contentAsString = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(contentAsString);
    }

    @Test
    public void testGetInteger() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/base/user/getInteger");
        String contentAsString = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(contentAsString);
    }

    @Test
    public void testGetIntegerWithRawType() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/base/user/getIntegerWithRawType");
        String contentAsString = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(contentAsString);
    }

    @Test
    public void testGetString() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/base/user/getString");
        String contentAsString = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(contentAsString);
    }

    @Test
    public void testGetById() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/base/user/getById/123");
        String contentAsString = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(contentAsString);
    }

    @Test
    public void testGetByIdMap() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/base/user/getByIdMap/123");
        String contentAsString = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(contentAsString);
    }

    @Test
    public void testGetByIdList() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/base/user/getByIdList");
        String contentAsString = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(contentAsString);
    }

    @Test
    public void testRequestBody() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/base/user/testRequestBody");
        requestBuilder.contentType(MediaType.APPLICATION_JSON_VALUE);
        requestBuilder.content("{\"userId\": \"zhangsan123\", \"username\": \"张三\"}");
        String contentAsString = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(contentAsString);
    }

    @Test
    public void testForm() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/base/user/testForm");
        requestBuilder.queryParam("userId", "zhangsan123");
        requestBuilder.queryParam("username", "张三");
        String contentAsString = mockMvc.perform(requestBuilder)
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(contentAsString);
    }

}
