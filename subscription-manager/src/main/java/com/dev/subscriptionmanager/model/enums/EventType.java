package com.dev.subscriptionmanager.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EventType {
    SUBSCRIPTION_CREATED("subscription_created"),
    PAYMENT_SUCCESS("payment_success"),
    PAYMENT_FAILED("payment_failed"),
    PLAN_CHANGED("plan_changed"),
    SUBSCRIPTION_UPGRADED("subscription_upgraded"),
    SUBSCRIPTION_DOWNGRADED("subscription_downgraded"),
    SUBSCRIPTION_CANCELED("subscription_canceled");

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
