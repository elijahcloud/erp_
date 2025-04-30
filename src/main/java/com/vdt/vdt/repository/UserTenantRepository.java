package com.vdt.vdt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vdt.vdt.entity.UserTenant;

public interface UserTenantRepository extends JpaRepository<UserTenant, Long> {
    boolean existsByUserIdAndTenantId(Long userId, Long tenantId);
}
