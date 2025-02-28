package com.store.order.controllers;

import com.store.order.utils.TestConstants;
import com.store.order.config.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KafkaProducerController.class)
@Import(TestSecurityConfig.class)
@TestPropertySource(properties = "spring.kafka.topic=" + TestConstants.TEST_KAFKA_TOPIC)
public class KafkaProducerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private KafkaTemplate<String, String> kafkaTemplate;

    private String topic;

    @BeforeEach
    void setUp() {
        topic = TestConstants.TEST_KAFKA_TOPIC;
    }

    @Test
    void publishMessageTest() throws Exception {
        String message = "Test message";

        mockMvc.perform(post("/api/kafka/publish")
                        .param("message", message))
                .andExpect(status().isOk());

        verify(kafkaTemplate, times(1)).send(topic, message);
    }
}