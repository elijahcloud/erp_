CREATE TABLE customers (
    customer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    phone_number VARCHAR(20),
    email VARCHAR(255),
    account_type VARCHAR(100),
    customer_tier VARCHAR(100),
    account_number VARCHAR(20),
    user_id_agent BIGINT,

    CONSTRAINT fk_customer_agent FOREIGN KEY (user_id_agent)
        REFERENCES users (user_id) ON DELETE SET NULL
);

CREATE TABLE customer_billing (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL UNIQUE,
    payment_method VARCHAR(100),
    billing_cycle VARCHAR(50),
    currency VARCHAR(10),
    tax_id VARCHAR(100),

    CONSTRAINT fk_customer_billing_customer
        FOREIGN KEY (customer_id)
        REFERENCES customers(customer_id)
        ON DELETE CASCADE
);

CREATE TABLE customer_notes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    note_content TEXT NOT NULL,
    date_created DATETIME NOT NULL,
    pinned BOOLEAN NOT NULL DEFAULT FALSE,
    user_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,

    CONSTRAINT fk_note_user FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT fk_note_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);
