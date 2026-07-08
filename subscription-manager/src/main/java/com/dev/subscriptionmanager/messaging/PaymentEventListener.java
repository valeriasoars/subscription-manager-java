package com.dev.subscriptionmanager.messaging;

import com.dev.subscriptionmanager.model.Event;
import com.dev.subscriptionmanager.model.Subscription;
import com.dev.subscriptionmanager.model.enums.EventType;
import com.dev.subscriptionmanager.model.enums.SubscriptionStatus;
import com.dev.subscriptionmanager.repository.EventRepository;
import com.dev.subscriptionmanager.repository.SubscriptionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class PaymentEventListener {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.FILA_PAGAMENTO)
    @Transactional
    public void receivePaymentEvent(String message) throws Exception {
        System.out.println("LOG [Worker Pagamento]: Mensagem recebida da fila!");

        JsonNode eventNode = objectMapper.readTree(message);
        UUID eventId = UUID.fromString(eventNode.get("id").asText());

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento não localizado no banco!"));

        if (event.isProcessed()) {
            System.out.println("LOG [Worker Pagamento]: Evento " + eventId + " já foi processado anteriormente. Ignorando duplicidade.");
            return;
        }

        EventType eventType = objectMapper.convertValue(eventNode.get("type"), EventType.class);
        JsonNode dataNode = objectMapper.readTree(eventNode.get("data").asText());
        UUID subscriptionId = UUID.fromString(dataNode.get("subscriptionId").asText());

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Assinatura não encontrada!"));

        if (eventType == EventType.PAYMENT_SUCCESS) {
            subscription.setStatus(SubscriptionStatus.ACTIVE);
            subscription.setNextBillingDate(LocalDate.now().plusMonths(1));
        } else if (eventType == EventType.PAYMENT_FAILED) {
            subscription.setStatus(SubscriptionStatus.SUSPENDED);
        }

        subscriptionRepository.save(subscription);

        event.setProcessed(true);
        eventRepository.save(event);
        System.out.println("LOG [Worker Pagamento]: Evento processado com sucesso!");
    }
}
