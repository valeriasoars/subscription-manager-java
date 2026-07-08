package com.dev.subscriptionmanager.messaging;

import com.dev.subscriptionmanager.model.Event;
import com.dev.subscriptionmanager.repository.EventRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SubscriptionCreatedListener {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.FILA_CRIACAO)
    public void receiveSubscriptionCreatedEvent(String message) {
        try {
            System.out.println("LOG [Worker Criação]: Nova assinatura detectada na fila!");

            JsonNode eventNode = objectMapper.readTree(message);
            UUID eventId = UUID.fromString(eventNode.get("id").asText());

            System.out.println("LOG [Worker Criação]: Enviando dados para o sistema de faturamento parceiro...");
            Thread.sleep(1500);

            Event event = eventRepository.findById(eventId).orElse(null);
            if (event != null) {
                event.setProcessed(true);
                eventRepository.save(event);
                System.out.println("LOG [Worker Criação]: Evento de criação processado e finalizado!");
            }

        } catch (Exception e) {
            System.err.println("ERRO ao processar criação de assinatura: " + e.getMessage());
        }
    }

}
