package com.dev.subscriptionmanager.messaging;

import com.dev.subscriptionmanager.model.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void publishEvent(Event event) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(event);
            String routingKey = getRoutingKey(event.getType().name());

            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_ASSINATURAS, routingKey, jsonPayload);
            System.out.println("LOG: Evento enviado ao RabbitMQ: " + event.getType());

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter evento para JSON", e);
        }
    }

    private String getRoutingKey(String eventType) {
        if ("SUBSCRIPTION_CREATED".equals(eventType)
                || "SUBSCRIPTION_CANCELED".equals(eventType)
                || "PLAN_CHANGED".equals(eventType)) {
            return RabbitMQConfig.ROUTING_KEY_CRIACAO;
        }
        return RabbitMQConfig.ROUTING_KEY_PAGAMENTO;
    }
}
