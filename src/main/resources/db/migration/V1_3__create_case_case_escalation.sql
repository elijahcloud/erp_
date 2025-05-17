CREATE TABLE cases
(
    case_id               BIGINT AUTO_INCREMENT NOT NULL,
    case_title            VARCHAR(255)          NULL,
    `description`         VARCHAR(255)          NULL,
    case_type             VARCHAR(255)          NULL,
    case_status           VARCHAR(255)          NULL,
    created_at            datetime              NULL,
    updated_at            datetime              NULL,
    resolved_at           datetime              NULL,
    last_escalated_at     datetime              NULL,
    sla_resolution_due_at datetime              NULL,
    escalation_level      VARCHAR(255)          NULL,
    customer_tier         VARCHAR(255)          NULL,
    issue_type            VARCHAR(255)          NULL,
    CONSTRAINT pk_cases PRIMARY KEY (case_id)
);


CREATE TABLE case_escalation_log
(
    id                  BIGINT       NOT NULL,
    parent_case_case_id BIGINT       NULL,
    reason              VARCHAR(255) NULL,
    from_level          SMALLINT     NULL,
    to_level            SMALLINT     NULL,
    escalated_by        VARCHAR(255) NULL,
    escalated_at        datetime     NULL,
    CONSTRAINT pk_caseescalationlog PRIMARY KEY (id)
);

ALTER TABLE case_escalation_log
    ADD CONSTRAINT FK_CASEESCALATIONLOG_ON_PARENTCASE_CASE FOREIGN KEY (parent_case_case_id) REFERENCES cases (case_id);