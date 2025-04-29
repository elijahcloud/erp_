package com.vdt.vdt.repository;

import com.vdt.vdt.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findFirstByRoleNameIgnoreCase(String name);
}