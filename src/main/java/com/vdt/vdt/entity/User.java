package com.vdt.vdt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_email", nullable = false, unique = true, length = 65)
    private String email;

    @Column(name = "user_password", nullable = false, length = 285)
    private String password;

    @Column(name = "user_is_active", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_created_by", columnDefinition = "BIGINT")
    @JsonIgnore
    private User createdBy;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<User> createdUsers;

    @Column(name = "user_created_at", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_updated_by", columnDefinition = "BIGINT")
    @JsonIgnore
    private User updatedBy;

    @OneToMany(mappedBy = "updatedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<User> updatedUsers;

    @Column(name = "user_updated_at", columnDefinition = "DATETIME")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "deletedBy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> deletedUsers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_deleted_by", columnDefinition = "BIGINT")
    @JsonIgnore
    private User deletedBy;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private Set<UserRole> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserRole> userRoles;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_active_user_role_id", referencedColumnName = "user_role_id", columnDefinition = "BIGINT")
    private UserRole activeRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserTenant> userTenants;

    @Column(name = "user_deleted_at", columnDefinition = "DATETIME")
    private LocalDateTime deletedAt;

    public User(String id) {
        this.id = Long.parseLong(id);
    }

    @PrePersist
    public void prePersist() {
        if (isActive == null) this.isActive = true;
        this.createdAt = LocalDateTime.now(ZoneId.of("Africa/Lagos"));
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now(ZoneId.of("Africa/Lagos"));
    }

    public void setActiveRole(Long roleId) {
        if (roles == null || roles.stream().noneMatch(role -> role.getId().equals(roleId))) {
            throw new IllegalArgumentException("Invalid role for the user");
        }
        this.activeRole = roles.stream()
                               .filter(role -> role.getId().equals(roleId))
                               .findFirst()
                               .orElseThrow(() -> new IllegalArgumentException("Invalid role for the user"));
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now(ZoneId.of("Africa/Lagos"));
        this.isActive = false;
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0; // Use only the ID to avoid recursion
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return getId() != null && getId().equals(user.getId()); // Compare only the ID
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                // Avoid including lazy-loaded collections like roles
                '}';
    }
}
