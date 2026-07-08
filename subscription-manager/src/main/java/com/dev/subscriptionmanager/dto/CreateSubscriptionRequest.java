package com.dev.subscriptionmanager.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateSubscriptionRequest {
    private UUID planId;
    private String customerEmail;
}
