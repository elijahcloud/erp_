CREATE TABLE sla_policies
(
    id                                BIGINT AUTO_INCREMENT NOT NULL,
    ticket_type                       VARCHAR(255)          NULL,
    priority                          VARCHAR(255)          NULL,
    customer_account_type             VARCHAR(255)          NULL,
    response_time_target_in_minutes   BIGINT                NOT NULL,
    resolution_time_target_in_minutes BIGINT                NOT NULL,
    created_at                        datetime              NULL,
    updated_at                        datetime              NULL,
    reassign_threshold_hours          INT                   NULL,
    deleted                           BIT(1)                NOT NULL,
    case_type                         VARCHAR(255)          NULL,
    customer_tier                     VARCHAR(255)          NULL,
    CONSTRAINT pk_sla_policies PRIMARY KEY (id)
);