package com.snorlacs.newse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@AutoConfigureMockMvc
public class ApiIntegrationTest {

    @Autowired
    private MockMvc mvc;

    protected ResultActions post(String url, String content, Object... urlVariables) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.post(url, urlVariables)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        );
    }

    protected ResultActions get(String url, Object... urlVariables) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.get(url, urlVariables).accept(MediaType.APPLICATION_JSON));
    }

    protected static String toJsonString(final Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

}
