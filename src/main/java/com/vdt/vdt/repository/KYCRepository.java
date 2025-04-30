package com.vdt.vdt.repository;

import com.vdt.vdt.entity.KYC;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface KYCRepository extends JpaRepository<KYC, Long> {
    
    // Find KYC by Customer ID
    Optional<KYC> findByCustomerId(Long customerId);

    // You can add more methods if needed
}
