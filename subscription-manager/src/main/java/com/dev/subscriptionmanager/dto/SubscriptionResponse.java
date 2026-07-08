package com.dev.subscriptionmanager.dto;

import com.dev.subscriptionmanager.model.enums.SubscriptionStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class SubscriptionResponse {
    private UUID id;
    private UUID planId;
    private String customerEmail;
    private SubscriptionStatus status;
    private LocalDate nextBillingDate;
}
