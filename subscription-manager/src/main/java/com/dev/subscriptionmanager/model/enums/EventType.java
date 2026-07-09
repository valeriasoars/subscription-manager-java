package com.dev.subscriptionmanager.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EventType {
    SUBSCRIPTION_CREATED("subscription_created"),
    PAYMENT_SUCCESS("payment_success"),
    PAYMENT_FAILED("payment_failed");

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
