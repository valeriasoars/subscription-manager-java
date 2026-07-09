package com.dev.subscriptionmanager.dto;

import com.dev.subscriptionmanager.model.enums.EventType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PaymentWebhookDTO {
    @JsonProperty("payment_id")
    @NotBlank(message = "payment_id é obrigatório")
    private String paymentId;

    @JsonProperty("subscription_id")
    @NotNull(message = "subscription_id é obrigatório")
    private UUID subscriptionId;

    @JsonProperty("event")
    @NotNull(message = "event é obrigatório")
    private EventType event;

    @JsonProperty("amount")
    private Double amount;

}
