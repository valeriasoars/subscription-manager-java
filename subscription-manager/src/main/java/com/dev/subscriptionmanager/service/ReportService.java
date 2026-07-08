package com.dev.subscriptionmanager.service;

import com.dev.subscriptionmanager.dto.PlanMetricDTO;
import com.dev.subscriptionmanager.dto.SubscriptionReportResponse;
import com.dev.subscriptionmanager.model.enums.SubscriptionStatus;
import com.dev.subscriptionmanager.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReportService {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Transactional(readOnly = true)
    public SubscriptionReportResponse getSubscriptionMetrics() {
        long totalActive = subscriptionRepository.countByStatus(SubscriptionStatus.ACTIVE);
        long totalCancelled = subscriptionRepository.countByStatus(SubscriptionStatus.CANCELED);

        List<PlanMetricDTO> planMetrics = subscriptionRepository.findActiveSubscriptionsGroupedByPlan();
        SubscriptionReportResponse report = new SubscriptionReportResponse();
        report.setTotalActive(totalActive);
        report.setTotalCancelled(totalCancelled);
        report.setPlans(planMetrics);

        return report;
    }
}
