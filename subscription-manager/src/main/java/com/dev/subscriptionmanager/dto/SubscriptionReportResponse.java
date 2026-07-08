package com.dev.subscriptionmanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SubscriptionReportResponse {
    @JsonProperty("total_active")
    private long totalActive;

    @JsonProperty("total_cancelled")
    private long totalCancelled;

    @JsonProperty("plans")
    private List<PlanMetricDTO> plans;
}
