package com.vdt.vdt.repository;

import com.vdt.vdt.entity.SubscriptionService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionServiceRepository extends JpaRepository<SubscriptionService,Long> {
    List<SubscriptionService> findByCustomerId(Long id);
}
