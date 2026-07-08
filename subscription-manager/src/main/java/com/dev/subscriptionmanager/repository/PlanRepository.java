package com.dev.subscriptionmanager.repository;

import com.dev.subscriptionmanager.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan, UUID> {
}
