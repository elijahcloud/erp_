package com.vdt.vdt.repository;

import com.vdt.vdt.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Optional<Tenant> findByName(String name);
    long countByIsActiveTrue();
    long countByIsActiveTrueAndCodeNot(String code);
    boolean existsByCodeAndIdNot(String code, Long id);
    boolean existsByCode(String code);

    @Query("SELECT td.domainName FROM TenantDomain td WHERE td.tenant.id = :tenantId")
    Optional<String> findTenantDomainNameByTenantId(@Param("tenantId") Long tenantId);
}