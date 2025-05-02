package com.vdt.vdt.repository;

import com.vdt.vdt.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    // Additional custom queries can be added here if needed

    @Transactional
    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.role.id = :roleId")
    void deleteByRoleId(Long roleId);
}
