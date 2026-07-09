package com.dev.subscriptionmanager.service;

import com.dev.subscriptionmanager.dto.CreatePlanRequest;
import com.dev.subscriptionmanager.exception.BusinessException;
import com.dev.subscriptionmanager.model.Plan;
import com.dev.subscriptionmanager.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlanService {
    @Autowired
    private PlanRepository planRepository;

    @Transactional
    public Plan createPlan(CreatePlanRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BusinessException("O nome do plano é obrigatório.");
        }

        if (request.getPrice() == null || request.getPrice() <= 0) {
            throw new BusinessException("O preço do plano deve ser maior que zero.");
        }

        if (request.getBillingCycle() == null) {
            throw new BusinessException("O ciclo de cobrança deve ser válido (MENSAL ou ANUAL).");
        }

        planRepository.findByNameIgnoreCase(request.getName().trim())
                .ifPresent(p -> {
                    throw new BusinessException("Já existe um plano cadastrado com este nome.");
                });

        Plan plan = new Plan();
        plan.setName(request.getName().trim());
        plan.setPrice(request.getPrice());
        plan.setBillingCycle(request.getBillingCycle());

        return planRepository.save(plan);
    }
}
