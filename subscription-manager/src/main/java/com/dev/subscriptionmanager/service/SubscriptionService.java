package com.dev.subscriptionmanager.service;

import com.dev.subscriptionmanager.dto.CreateSubscriptionRequest;
import com.dev.subscriptionmanager.dto.PaymentWebhookDTO;
import com.dev.subscriptionmanager.dto.PlanChangeRequest;
import com.dev.subscriptionmanager.dto.SubscriptionResponse;
import com.dev.subscriptionmanager.exception.BusinessException;
import com.dev.subscriptionmanager.messaging.EventPublisher;
import com.dev.subscriptionmanager.model.Event;
import com.dev.subscriptionmanager.model.Plan;
import com.dev.subscriptionmanager.model.Subscription;
import com.dev.subscriptionmanager.model.enums.BillingCycle;
import com.dev.subscriptionmanager.model.enums.EventType;
import com.dev.subscriptionmanager.model.enums.SubscriptionStatus;
import com.dev.subscriptionmanager.repository.EventRepository;
import com.dev.subscriptionmanager.repository.PlanRepository;
import com.dev.subscriptionmanager.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
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
        if (request.getCustomerEmail() == null || request.getCustomerEmail().trim().isEmpty()) {
            throw new BusinessException("O e-mail do cliente é obrigatório.");
        }

        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new BusinessException("O plano informado não existe."));

        subscriptionRepository.findByCustomerEmailAndPlanIdAndStatusIn(
                request.getCustomerEmail().trim(),
                plan.getId(),
                List.of(SubscriptionStatus.PENDING, SubscriptionStatus.ACTIVE)
        ).ifPresent(s -> {
            throw new BusinessException("O cliente já possui uma assinatura ativa ou pendente para este plano.");
        });

        Subscription subscription = new Subscription();
        subscription.setPlanId(request.getPlanId());
        subscription.setCustomerEmail(request.getCustomerEmail());
        subscription.setStatus(SubscriptionStatus.PENDING);

        if (plan.getBillingCycle() == BillingCycle.MENSAL) {
            subscription.setNextBillingDate(LocalDate.now().plusMonths(1));
        } else if (plan.getBillingCycle() == BillingCycle.ANUAL) {
            subscription.setNextBillingDate(LocalDate.now().plusYears(1));
        }


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

    private static final double TOLERANCIA = 0.01;
    @Transactional
    public void receivePaymentWebhook(PaymentWebhookDTO webhookDTO) {
        Subscription subscription = subscriptionRepository.findById(webhookDTO.getSubscriptionId())
                .orElseThrow(() -> new BusinessException("Assinatura não encontrada."));

        if (subscription.getStatus() == SubscriptionStatus.CANCELED) {
            throw new BusinessException("Não é possível processar pagamento de uma assinatura cancelada.");
        }

        if (subscription.getStatus() == SubscriptionStatus.EXPIRED) {
            throw new BusinessException("Não é possível processar pagamento de uma assinatura expirada.");
        }

        if (eventRepository.existsByExternalPaymentId(webhookDTO.getPaymentId())) {
            throw new BusinessException("Este evento de pagamento já foi recebido anteriormente.");
        }

        Plan plan = planRepository.findById(subscription.getPlanId())
                .orElseThrow(() -> new BusinessException("Plano da assinatura não encontrado."));


        if (webhookDTO.getAmount() == null || Math.abs(webhookDTO.getAmount() - plan.getPrice()) > TOLERANCIA) {
            throw new BusinessException("O valor pago (" + webhookDTO.getAmount()
                    + ") não corresponde ao valor do plano (" + plan.getPrice() + ").");
        }

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
                .orElseThrow(() -> new BusinessException("Assinatura não encontrada!"));

        Plan newPlan = planRepository.findById(request.getNewPlanId())
                .orElseThrow(() -> new BusinessException("Novo plano não encontrado!"));

        subscription.setPlanId(request.getNewPlanId());

        if (newPlan.getBillingCycle() == BillingCycle.MENSAL) {
            subscription.setNextBillingDate(LocalDate.now().plusMonths(1));
        } else if (newPlan.getBillingCycle() == BillingCycle.ANUAL) {
            subscription.setNextBillingDate(LocalDate.now().plusYears(1));
        }

        subscriptionRepository.save(subscription);

        Event event = new Event();
        event.setType(EventType.PLAN_CHANGED);
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
        event.setType(EventType.SUBSCRIPTION_CANCELED);
        event.setData("{\"subscriptionId\":\"" + subscription.getId() + "\",\"action\":\"canceled\"}");
        event.setProcessed(false);
        eventRepository.save(event);
        eventPublisher.publishEvent(event);
    }


    private SubscriptionResponse convertToResponse(Subscription sub) {
        SubscriptionResponse response = new SubscriptionResponse();
        response.setId(sub.getId());
        response.setStatus(sub.getStatus());
        response.setNextBillingDate(sub.getNextBillingDate());
        return response;
    }
}
