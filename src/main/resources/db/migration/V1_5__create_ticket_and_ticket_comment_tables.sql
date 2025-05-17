-- ============================
-- v1__create_ticket.sql
-- Database Schema Initialization
-- ============================

-- Tickets Table

CREATE TABLE tickets
(
    ticket_id                     BIGINT AUTO_INCREMENT NOT NULL,
    ticket_customer_id            BIGINT                NOT NULL,
    ticket_title                  VARCHAR(255)          NOT NULL,
    ticket_type                   ENUM('COMPLAINT', 'SERVICE_REQUEST', 'INFO_REQUEST', 'TECHNICAL_SUPPORT') NULL,
    ticket_description            VARCHAR(255)          NULL,
    ticket_priority              ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL') NULL,
    ticket_status                ENUM('OPEN', 'ASSIGNED', 'IN_PROGRESS', 'ESCALATED', 'RESOLVED', 'CLOSED') NULL,
    ticket_source                 VARCHAR(255)          NULL,
    ticket_assigned_department_id BIGINT                NULL,
    ticket_assigned_agent         VARCHAR(255)          NULL,
    ticket_created_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ticket_created_by             BIGINT                NULL,
    ticket_updated_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    ticket_updated_by             BIGINT                NULL,
    ticket_deleted                BOOLEAN DEFAULT FALSE NOT NULL,
    ticket_deleted_at             DATETIME DEFAULT NULL,
    ticket_deleted_by             BIGINT                NULL,
    last_action_at                DATETIME DEFAULT NULL,
    first_response_at             DATETIME DEFAULT NULL,
    resolved_at                   DATETIME DEFAULT NULL,
    sla_response_due_at           DATETIME DEFAULT NULL,
    sla_resolution_due_at         DATETIME DEFAULT NULL,
    is_response_sla_breached      BOOLEAN DEFAULT FALSE NOT NULL,
    is_resolution_sla_breached    BOOLEAN DEFAULT FALSE NOT NULL,
    sla_timer_paused              BOOLEAN DEFAULT FALSE NOT NULL,
    pause_reason                  VARCHAR(255)          NULL,
    sla_paused_at                 DATETIME DEFAULT NULL,
    response_time_minutes         BIGINT                NOT NULL,
    resolution_time_minutes       BIGINT                NOT NULL,
    ticket_sla_status             ENUM('ON_TRACK','AT_RISK', 'BREACHED'),
    ticket_case_id                BIGINT                NULL,
    issue_type                    ENUM('SIM_FAILURE', 'BULK_SIM_MIGRATION', 'NETWORK_OUTAGE', 'DEVICE_PROVISIONING', 'ACCOUNT_MIGRATION') NULL,
    CONSTRAINT pk_tickets PRIMARY KEY (ticket_id)
);


-- Ticket Comments Table
CREATE TABLE ticket_comments
(
    ticket_comment_id         BIGINT AUTO_INCREMENT NOT NULL,
    ticket_comment_text       VARCHAR(255)          NOT NULL,
    ticket_comment_author     VARCHAR(255)          NULL,
    ticket_comment_timestamp  datetime              NOT NULL,
    ticket_comment_ticket_id  BIGINT                NOT NULL,
    ticket_id                 BIGINT                NOT NULL,
    ticket_comment_created_by BIGINT                NULL,
    ticket_comment_updated_at datetime              NULL,
    ticket_comment_updated_by BIGINT                NULL,
    ticket_comment_deleted_at datetime              NULL,
    ticket_comment_deleted_by BIGINT                NULL,
    CONSTRAINT pk_ticket_comments PRIMARY KEY (ticket_comment_id)
);

ALTER TABLE ticket_comments
    ADD CONSTRAINT FK_TICKET_COMMENTS_ON_TICKET_COMMENT_TICKET FOREIGN KEY (ticket_comment_ticket_id) REFERENCES tickets (ticket_id);


-- Add Foreign Keys for tickets
ALTER TABLE tickets
    ADD CONSTRAINT FK_TICKETS_ON_TICKET_ASSIGNED_DEPARTMENT FOREIGN KEY (ticket_assigned_department_id) REFERENCES departments (department_id);

ALTER TABLE tickets
    ADD CONSTRAINT FK_TICKETS_ON_TICKET_CASE FOREIGN KEY (ticket_case_id) REFERENCES cases (case_id);

ALTER TABLE tickets
    ADD CONSTRAINT FK_TICKETS_ON_TICKET_CUSTOMER FOREIGN KEY (ticket_customer_id) REFERENCES customers (customer_id);

