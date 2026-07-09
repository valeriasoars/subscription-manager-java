package com.dev.subscriptionmanager.dto;

import com.dev.subscriptionmanager.model.enums.BillingCycle;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreatePlanRequest {
    @JsonProperty("name")
    private String name;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("billing_cycle")
    private BillingCycle billingCycle;
}
