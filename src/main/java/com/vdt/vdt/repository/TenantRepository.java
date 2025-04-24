package com.vdt.vdt.repository;

import com.vdt.vdt.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Optional<Tenant> findByName(String name);
    long countByIsActiveTrue();
    long countByIsActiveTrueAndCodeNot(String code);
}