package com.dev.subscriptionmanager.dto;

import com.dev.subscriptionmanager.model.enums.EventType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class PaymentWebhookDTO {
    @JsonProperty("subscription_id")
    private UUID subscriptionId;

    @JsonProperty("event")
    private EventType event;

    @JsonProperty("amount")
    private Double amount;

    @JsonProperty("date")
    private String date;
}
