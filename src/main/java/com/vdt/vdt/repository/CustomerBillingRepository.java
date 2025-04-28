package com.vdt.vdt.repository;

import com.vdt.vdt.entity.CustomerBilling;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerBillingRepository extends JpaRepository<CustomerBilling, Long> {
    Optional<CustomerBilling> findByCustomerId(Long customerId);
}
