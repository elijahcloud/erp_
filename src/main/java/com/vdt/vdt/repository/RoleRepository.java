package com.vdt.vdt.repository;

import com.vdt.vdt.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    @Query("SELECT COUNT(ur) FROM UserRole ur WHERE ur.role.id = :roleId")
    Long countUsersByRoleId(@Param("roleId") Long roleId);

    Optional<Role> findById(Long id);

    @Modifying
    @Query(value = "INSERT INTO role_permission (role_id, permission_id) VALUES (:roleId, :permissionId)", nativeQuery = true)
    void assignPermissionToRole(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);
}
