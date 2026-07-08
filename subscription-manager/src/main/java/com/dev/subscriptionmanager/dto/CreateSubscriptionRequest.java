package com.dev.subscriptionmanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateSubscriptionRequest {
    @JsonProperty("plan_id")
    private UUID planId;

    @JsonProperty("customer_email")
    private String customerEmail;
}
