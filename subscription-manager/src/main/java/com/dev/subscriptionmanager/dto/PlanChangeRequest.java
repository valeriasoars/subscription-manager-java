package com.dev.subscriptionmanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class PlanChangeRequest {
    @JsonProperty("new_plan_id")
    private UUID newPlanId;
}
