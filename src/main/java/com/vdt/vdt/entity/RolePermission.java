package com.vdt.vdt.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role_permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_permission_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_permission_role_id", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "role_permission_permission_id", nullable = false)
    private Permission permission;
}
