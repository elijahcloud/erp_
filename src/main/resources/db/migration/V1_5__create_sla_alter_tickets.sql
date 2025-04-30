

CREATE TABLE sla_policies
(
    id                       BIGINT AUTO_INCREMENT NOT NULL,
    ticket_type              VARCHAR(255)          NULL,
    priority                 VARCHAR(255)          NULL,
    customer_group           SMALLINT              NULL,
    response_time_target     BIGINT                NULL,
    resolution_time_target   BIGINT                NULL,
    created_at               datetime              NULL,
    updated_at               datetime              NULL,
    reassign_threshold_hours INT                   NULL,
    CONSTRAINT pk_sla_policies PRIMARY KEY (id)
);


CREATE INDEX idx_sla_ticket_type ON sla_policies(ticket_type);
CREATE INDEX idx_sla_priority ON sla_policies(priority);
CREATE INDEX idx_sla_customer_group ON sla_policies(customer_group);


ALTER TABLE tickets
ADD COLUMN sla_status ENUM('ON_TRACK', 'BREACHED') DEFAULT 'ON_TRACK',
ADD COLUMN last_action_at DATETIME DEFAULT NULL,
ADD COLUMN first_response_at DATETIME DEFAULT NULL,
ADD COLUMN resolved_at DATETIME DEFAULT NULL,
ADD COLUMN sla_response_due_at DATETIME DEFAULT NULL,
ADD COLUMN sla_resolution_due_at DATETIME DEFAULT NULL,
ADD COLUMN is_response_sla_breached BOOLEAN DEFAULT FALSE,
ADD COLUMN is_resolution_sla_breached BOOLEAN DEFAULT FALSE,
ADD COLUMN sla_timer_paused BOOLEAN DEFAULT FALSE,
ADD COLUMN pause_reason VARCHAR(255) DEFAULT NULL,
ADD COLUMN sla_paused_at DATETIME DEFAULT NULL;
