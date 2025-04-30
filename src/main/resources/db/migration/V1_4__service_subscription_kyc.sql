CREATE TABLE services (
    erp_service_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    service_name VARCHAR(255),
    linked_branch VARCHAR(255),
    linked_zone VARCHAR(255),
    tenant_id_ref BIGINT,

    CONSTRAINT fk_services_tenant
        FOREIGN KEY (tenant_id_ref)
        REFERENCES tenants (tenant_id)
        ON DELETE SET NULL
);

CREATE TABLE prices (
    price_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    amount DOUBLE NOT NULL,
    created_at DATETIME NOT NULL,

    erp_service_id BIGINT,

    CONSTRAINT fk_prices_service
        FOREIGN KEY (erp_service_id)
        REFERENCES services (erp_service_id)
        ON DELETE CASCADE
);

CREATE TABLE kyc (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    kyc_package VARCHAR(50),
    kyc_status VARCHAR(50),
    last_updated DATETIME,
    document LONGBLOB,
    kyc_customer_id BIGINT NOT NULL UNIQUE,

    CONSTRAINT fk_kyc_customer
        FOREIGN KEY (kyc_customer_id)
        REFERENCES customers (customer_id)
        ON DELETE CASCADE
);


CREATE TABLE subscription_services (
    subscription_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    billing_cycle VARCHAR(50),
    tenant_id_ref BIGINT,
    customer_id_ref BIGINT,
    erp_service_type_id_ref BIGINT,
    created_at DATETIME NOT NULL,

    CONSTRAINT fk_subscription_tenant
        FOREIGN KEY (tenant_id_ref)
        REFERENCES tenants (tenant_id)
        ON DELETE SET NULL,

    CONSTRAINT fk_subscription_customer
        FOREIGN KEY (customer_id_ref)
        REFERENCES customers (customer_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_service_type
        FOREIGN KEY (erp_service_type_id_ref)
        REFERENCES services (erp_service_id)
);


CREATE TABLE customer_celebrations (
    celebration_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    celebration_type VARCHAR(255),
    custom_label VARCHAR(255),
    date DATE NOT NULL,
    reminder_in_days BIGINT,
    note TEXT,
    customer_id_ref BIGINT,

    CONSTRAINT fk_customer
        FOREIGN KEY (customer_id_ref)
        REFERENCES customers (customer_id)
);




