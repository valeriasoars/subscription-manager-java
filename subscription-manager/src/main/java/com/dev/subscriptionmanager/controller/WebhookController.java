package com.dev.subscriptionmanager.controller;

import com.dev.subscriptionmanager.dto.PaymentWebhookDTO;
import com.dev.subscriptionmanager.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {
    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/payment")
    public ResponseEntity<Void> handlePaymentWebhook(@RequestBody PaymentWebhookDTO webhookDTO) {
        subscriptionService.receivePaymentWebhook(webhookDTO);

        return ResponseEntity.ok().build();
    }
}
