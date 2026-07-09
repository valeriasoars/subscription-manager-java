package com.dev.subscriptionmanager.service;

import com.dev.subscriptionmanager.dto.CreateSubscriptionRequest;
import com.dev.subscriptionmanager.dto.PaymentWebhookDTO;
import com.dev.subscriptionmanager.dto.PlanChangeRequest;
import com.dev.subscriptionmanager.dto.SubscriptionResponse;
import com.dev.subscriptionmanager.messaging.EventPublisher;
import com.dev.subscriptionmanager.model.Event;
import com.dev.subscriptionmanager.model.Subscription;
import com.dev.subscriptionmanager.model.enums.EventType;
import com.dev.subscriptionmanager.model.enums.SubscriptionStatus;
import com.dev.subscriptionmanager.repository.EventRepository;
import com.dev.subscriptionmanager.repository.PlanRepository;
import com.dev.subscriptionmanager.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventPublisher eventPublisher;

    @Transactional
    public SubscriptionResponse createSubscription(CreateSubscriptionRequest request) {
        planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new RuntimeException("Plano não encontrado!"));

        Subscription subscription = new Subscription();
        subscription.setPlanId(request.getPlanId());
        subscription.setCustomerEmail(request.getCustomerEmail());
        subscription.setStatus(SubscriptionStatus.PENDING);
        subscription = subscriptionRepository.save(subscription);

        Event event = new Event();
        event.setType(EventType.SUBSCRIPTION_CREATED);
        event.setData("{\"subscriptionId\":\""
                + subscription.getId()
                + "\",\"email\":\""
                + subscription.getCustomerEmail() + "\",\"type\":\""
                + EventType.SUBSCRIPTION_CREATED.getValue() + "\"}");
        event.setProcessed(false);
        eventRepository.save(event);

        eventPublisher.publishEvent(event);

        return convertToResponse(subscription);
    }

    private SubscriptionResponse convertToResponse(Subscription sub) {
        SubscriptionResponse response = new SubscriptionResponse();
        response.setId(sub.getId());
        response.setStatus(sub.getStatus());
        response.setNextBillingDate(sub.getNextBillingDate());
        return response;
    }

    @Transactional
    public void receivePaymentWebhook(PaymentWebhookDTO webhookDTO) {
        Event event = new Event();
        event.setType(webhookDTO.getEvent());
        event.setData("{\"subscriptionId\":\""
                + webhookDTO.getSubscriptionId()
                + "\",\"amount\":"
                + webhookDTO.getAmount()
                + ",\"type\":\"" + webhookDTO.getEvent().getValue() + "\"}");
        event.setProcessed(false);

        eventRepository.save(event);

        eventPublisher.publishEvent(event);
        System.out.println("LOG: Webhook de pagamento recebido usando Enum e enviado para a fila!");
    }

    @Transactional
    public SubscriptionResponse changePlan(UUID subscriptionId, PlanChangeRequest request) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Assinatura não encontrada!"));

        planRepository.findById(request.getNewPlanId())
                .orElseThrow(() -> new RuntimeException("Novo plano não encontrado!"));

        subscription.setPlanId(request.getNewPlanId());
        subscriptionRepository.save(subscription);

        Event event = new Event();
        event.setType(EventType.SUBSCRIPTION_CREATED);
        event.setData("{\"subscriptionId\":\"" + subscription.getId() + "\",\"action\":\"plan_changed\",\"newPlanId\":\"" + request.getNewPlanId() + "\"}");
        eventRepository.save(event);
        eventPublisher.publishEvent(event);

        return convertToResponse(subscription);
    }

    @Transactional
    public void cancelSubscription(UUID subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new RuntimeException("Assinatura não encontrada!"));

        subscription.setStatus(SubscriptionStatus.CANCELED);
        subscriptionRepository.save(subscription);

        Event event = new Event();
        event.setType(EventType.SUBSCRIPTION_CREATED);
        event.setData("{\"subscriptionId\":\"" + subscription.getId() + "\",\"action\":\"canceled\"}");
        eventRepository.save(event);
        eventPublisher.publishEvent(event);
    }
}
