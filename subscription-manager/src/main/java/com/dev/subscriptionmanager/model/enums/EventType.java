package com.dev.subscriptionmanager.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EventType {
    @JsonProperty("subscription_created")
    SUBSCRIPTION_CREATED,
    @JsonProperty("payment_success")
    PAYMENT_SUCCESS,
    @JsonProperty("payment_failed")
    PAYMENT_FAILED
}
