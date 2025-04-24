package com.vdt.vdt.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_log_id")
    private Long id;

    @Column(name = "audit_log_table_name", length = 255)
    private String tableName;

    @Column(name = "audit_log_action", nullable = false, length = 10)
    private String action;

    @Column(name = "audit_log_record_id")
    private Long recordId;

    @Column(name = "audit_log_data_before", columnDefinition = "JSON")
    private String dataBefore;

    @Column(name = "audit_log_data_after", columnDefinition = "JSON")
    private String dataAfter;

    @ManyToOne
    @JoinColumn(name = "audit_log_performed_by")
    private User performedBy;

    @Column(name = "audit_log_performed_at", nullable = false)
    private LocalDateTime performedAt;
}
