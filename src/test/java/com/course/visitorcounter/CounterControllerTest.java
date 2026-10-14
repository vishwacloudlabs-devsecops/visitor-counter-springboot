package com.course.visitorcounter;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CounterController.class)
class CounterControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private StringRedisTemplate redis;

    @MockitoBean
    private ValueOperations<String, String> ops;

    @BeforeEach
    void setUp() {
        when(redis.opsForValue()).thenReturn(ops);
    }

    @Test
    void indexIncrementsAndShowsCounter() throws Exception {
        when(ops.increment(CounterController.KEY)).thenReturn(7L);
        mvc.perform(get("/"))
           .andExpect(status().isOk())
           .andExpect(content().string(containsString("Visitor Counter")))
           .andExpect(content().string(containsString(">7<")));
    }

    @Test
    void apiVisitsReturnsZeroWhenKeyMissing() throws Exception {
        when(ops.get(CounterController.KEY)).thenReturn(null);
        mvc.perform(get("/api/visits"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.visits").value(0));
    }

    @Test
    void apiVisitsReturnsStoredValue() throws Exception {
        when(ops.get(CounterController.KEY)).thenReturn("42");
        mvc.perform(get("/api/visits"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.visits").value(42));
    }

    @Test
    void versionEndpointReturnsVersionAndEnvironment() throws Exception {
        mvc.perform(get("/version"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.version").exists())
           .andExpect(jsonPath("$.environment").exists());
    }
}
