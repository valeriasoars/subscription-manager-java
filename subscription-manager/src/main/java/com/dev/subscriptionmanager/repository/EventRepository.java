package com.dev.subscriptionmanager.repository;

import com.dev.subscriptionmanager.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    boolean existsByExternalPaymentId(String externalPaymentId);
}
