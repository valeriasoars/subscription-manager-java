package com.dev.subscriptionmanager.dto;

import com.dev.subscriptionmanager.model.enums.SubscriptionStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class SubscriptionResponse {
    @JsonProperty("subscription_id")
    private UUID id;

    @JsonProperty("status")
    private SubscriptionStatus status;

    @JsonProperty("next_billing_date")
    private LocalDate nextBillingDate;
}
