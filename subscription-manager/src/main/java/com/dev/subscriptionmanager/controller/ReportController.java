package com.dev.subscriptionmanager.controller;

import com.dev.subscriptionmanager.dto.SubscriptionReportResponse;
import com.dev.subscriptionmanager.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/subscriptions")
    public ResponseEntity<SubscriptionReportResponse> getSubscriptionMetrics() {
        SubscriptionReportResponse response = reportService.getSubscriptionMetrics();
        return ResponseEntity.ok(response);
    }
}
