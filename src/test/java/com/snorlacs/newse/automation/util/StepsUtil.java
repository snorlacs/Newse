package com.snorlacs.newse.automation.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snorlacs.newse.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Base64Utils;

import java.io.IOException;

@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application.properties")
public abstract class StepsUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    @Value("${editor.username}")
    private String username;

    @Value("${editor.password}")
    private String password;

    @Autowired
    private MockMvc mvc;

    private MockHttpServletResponse lastGetResponse;
    private MockHttpServletResponse lastPostResponse;
    private MockHttpServletResponse lastPutResponse;
    private MockHttpServletResponse lastDeleteResponse;
    private int lastStatusCode;

    protected void get(String url, Object... urlVariables) throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(url, urlVariables)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    lastGetResponse = result.getResponse();
                    lastStatusCode = lastGetResponse.getStatus();
                });
    }

    protected void post(String url, String body, Object... urlVariables) throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(url, urlVariables).header(HttpHeaders.AUTHORIZATION,
                "Basic " + Base64Utils.encodeToString(getAuthorizationSecretInBytes()))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andDo(result -> {
                    lastPostResponse = result.getResponse();
                    lastStatusCode = lastPostResponse.getStatus();
                });
    }

    protected void put(String url, String body, Object... urlVariables) throws Exception {
        mvc.perform(MockMvcRequestBuilders.put(url, urlVariables).header(HttpHeaders.AUTHORIZATION,
                "Basic " + Base64Utils.encodeToString(getAuthorizationSecretInBytes()))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andDo(result -> {
                    lastPutResponse = result.getResponse();
                    lastStatusCode = lastPutResponse.getStatus();
                });
    }

    protected void delete(String url, Object... urlVariables) throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(url, urlVariables).header(HttpHeaders.AUTHORIZATION,
                "Basic " + Base64Utils.encodeToString(getAuthorizationSecretInBytes()))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    lastDeleteResponse = result.getResponse();
                    lastStatusCode = lastDeleteResponse.getStatus();
                });
    }

    protected MockHttpServletResponse getLastGetResponse() {
        return lastGetResponse;
    }

    protected <T> T getLastGetContentAs(TypeReference<T> type) throws Exception {
        return deserializeResponse(getLastGetResponse(), type);
    }

    protected <T> T getLastPostContentAs(TypeReference<T> type) throws Exception {
        return deserializeResponse(getLastPostResponse(), type);
    }

    protected <T> T getLastPutContentAs(TypeReference<T> type) throws Exception {
        return deserializeResponse(getLastPutResponse(), type);
    }

    private static <T> T deserializeResponse(MockHttpServletResponse response, TypeReference<T> type) throws Exception {
        return deserialize(response.getContentAsString(), type);
    }

    protected MockHttpServletResponse getLastPostResponse() {
        return lastPostResponse;
    }

    protected MockHttpServletResponse getLastPutResponse() {
        return lastPutResponse;
    }

    protected static <T> T deserialize(String json, TypeReference<T> type) throws JsonParseException, JsonMappingException, IOException {
        return mapper.readValue(json, type);
    }

    public int getLastStatusCode() {
        return lastStatusCode;
    }

    public byte[] getAuthorizationSecretInBytes() {
        String authorizationString = username + ":" + password;
        return authorizationString.getBytes();
    }
}
