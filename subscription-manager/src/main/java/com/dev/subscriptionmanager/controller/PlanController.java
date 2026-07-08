package com.dev.subscriptionmanager.controller;


import com.dev.subscriptionmanager.model.Plan;
import com.dev.subscriptionmanager.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
public class PlanController {
    @Autowired
    private PlanRepository planRepository;

    @PostMapping
    public ResponseEntity<Plan> createPlan(@RequestBody Plan plan) {
        Plan saved = planRepository.save(plan);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Plan>> listPlans() {
        return ResponseEntity.ok(planRepository.findAll());
    }
}
