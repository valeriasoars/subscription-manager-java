package com.dev.subscriptionmanager.repository;

import com.dev.subscriptionmanager.dto.PlanMetricDTO;
import com.dev.subscriptionmanager.model.Subscription;
import com.dev.subscriptionmanager.model.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    long countByStatus(SubscriptionStatus status);
    @Query("SELECT new com.dev.subscriptionmanager.dto.PlanMetricDTO(p.id, p.name, COUNT(s)) " +
            "FROM Subscription s JOIN Plan p ON s.planId = p.id " +
            "WHERE s.status = com.dev.subscriptionmanager.model.enums.SubscriptionStatus.ACTIVE " +
            "GROUP BY p.id, p.name")
    List<PlanMetricDTO> findActiveSubscriptionsGroupedByPlan();

    Optional<Subscription> findByCustomerEmailAndPlanIdAndStatusIn(
            String customerEmail,
            UUID planId,
            List<SubscriptionStatus> statuses
    );
}
