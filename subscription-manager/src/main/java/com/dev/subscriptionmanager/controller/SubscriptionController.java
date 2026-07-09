package com.dev.subscriptionmanager.controller;

import com.dev.subscriptionmanager.dto.CreateSubscriptionRequest;
import com.dev.subscriptionmanager.dto.PlanChangeRequest;
import com.dev.subscriptionmanager.dto.SubscriptionResponse;
import com.dev.subscriptionmanager.service.SubscriptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionResponse> createSubscription(@Valid @RequestBody CreateSubscriptionRequest request) {
        SubscriptionResponse response = subscriptionService.createSubscription(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/change-plan")
    public ResponseEntity<SubscriptionResponse> changePlan(@PathVariable UUID id, @RequestBody PlanChangeRequest request) {
        SubscriptionResponse response = subscriptionService.changePlan(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelSubscription(@PathVariable UUID id) {
        subscriptionService.cancelSubscription(id);
        return ResponseEntity.noContent().build();
    }
}
