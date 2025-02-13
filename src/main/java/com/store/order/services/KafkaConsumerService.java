package com.store.order.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.order.commons.Constants;
import com.store.order.commons.kafka.events.ProductUpdateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Profile("kafka-enabled")
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(topics = "${spring.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String event) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(event);
        String eventType = rootNode.get("eventType").asText();

        switch (eventType) {
            case Constants.PRODUCT_UPDATED:
                ProductUpdateEvent productUpdateEvent = objectMapper.readValue(event, ProductUpdateEvent.class);
                log.debug("Product updated: " + productUpdateEvent);
                break;
            default:
                log.debug("Unknown event type: " + eventType);
        }

    }
}