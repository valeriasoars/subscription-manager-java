package com.dev.subscriptionmanager.controller;


import com.dev.subscriptionmanager.dto.CreatePlanRequest;
import com.dev.subscriptionmanager.model.Plan;
import com.dev.subscriptionmanager.repository.PlanRepository;
import com.dev.subscriptionmanager.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
public class PlanController {
    @Autowired
    private PlanService planService;

    @PostMapping
    public ResponseEntity<Plan> createPlan(@RequestBody CreatePlanRequest request) {
        Plan createdPlan = planService.createPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlan);
    }

}
