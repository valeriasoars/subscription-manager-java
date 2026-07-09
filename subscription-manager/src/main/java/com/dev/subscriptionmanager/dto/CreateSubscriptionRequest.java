package com.dev.subscriptionmanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateSubscriptionRequest {
    @JsonProperty("plan_id")
    private UUID planId;

    @JsonProperty("customer_email")
    @NotBlank(message = "customer_email é obrigatório")
    @Email(message = "customer_email deve ser um e-mail válido")
    private String customerEmail;
}
