package com.vdt.vdt.repository;

import com.vdt.vdt.entity.UserRole;
import com.vdt.vdt.entity.User; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findAllByUser(User user);
    boolean existsByUserIdAndTenantIdAndRoleId(Long userId, Long tenantId, Long roleId);
}