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
    public void receiveSubscriptionCreatedEvent(String message) throws Exception {
        System.out.println("LOG [Worker Criação]: Nova assinatura detectada na fila!");

        JsonNode eventNode = objectMapper.readTree(message);
        UUID eventId = UUID.fromString(eventNode.get("id").asText());

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento não localizado no banco!"));

        if (event.isProcessed()) {
            System.out.println("LOG [Worker Criação]: Evento " + eventId + " já foi processado. Ignorando.");
            return;
        }

        System.out.println("LOG [Worker Criação]: Enviando dados para o sistema parceiro...");
        Thread.sleep(1500);

        event.setProcessed(true);
        eventRepository.save(event);
        System.out.println("LOG [Worker Criação]: Evento de criação finalizado com sucesso!");
    }

}
