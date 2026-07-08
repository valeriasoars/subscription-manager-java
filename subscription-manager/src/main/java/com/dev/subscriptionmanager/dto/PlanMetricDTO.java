package com.dev.subscriptionmanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PlanMetricDTO {
    @JsonProperty("plan_id")
    private UUID planId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("active_subscriptions")
    private long activeSubscriptions;
}
