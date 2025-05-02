package com.vdt.vdt.repository;

import com.vdt.vdt.entity.TenantSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TenantSettingRepository extends JpaRepository<TenantSetting, Long> {
    TenantSetting findByTenantSettingTenantId(Long tenantSettingTenantId);
}