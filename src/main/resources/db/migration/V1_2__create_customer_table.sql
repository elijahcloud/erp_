-- 1. Create customers table
CREATE TABLE customers (
    customer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_tenant_id BIGINT NOT NULL, -- Link to which tenant (organization) the customer belongs
    customer_name VARCHAR(155) NOT NULL,
    customer_phone_number VARCHAR(20) NOT NULL,
    customer_email VARCHAR(65) UNIQUE NOT NULL,
    customer_account_type VARCHAR(100) NOT NULL,
    customer_tier VARCHAR(100),
    company_name VARCHAR(255) NULL,
    customer_account_number VARCHAR(20) NOT NULL,
    customer_user_id_agent BIGINT, -- (Optional) Staff assigned to manage customer
    customer_created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    customer_created_by BIGINT DEFAULT NULL,
    customer_updated_at DATETIME DEFAULT NULL,
    customer_updated_by BIGINT DEFAULT NULL,
    customer_deleted_at DATETIME DEFAULT NULL,
    customer_deleted_by BIGINT DEFAULT NULL
);

-- 2. Create customer_billings table
CREATE TABLE customer_billings (
    customer_billing_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_billing_customer_id BIGINT NOT NULL UNIQUE, -- One-to-one relationship with customers
    customer_billing_payment_method VARCHAR(100),
    customer_billing_billing_cycle VARCHAR(50),
    customer_billing_currency VARCHAR(20),
    customer_billing_tax_id VARCHAR(50),
    customer_billing_created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    customer_billing_created_by BIGINT DEFAULT NULL,
    customer_billing_updated_at DATETIME DEFAULT NULL,
    customer_billing_updated_by BIGINT DEFAULT NULL,
    customer_billing_deleted_at DATETIME DEFAULT NULL,
    customer_billing_deleted_by BIGINT DEFAULT NULL
);

-- 3. Create customer_notes table
CREATE TABLE customer_notes (
    customer_note_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_note_content TEXT NOT NULL,
    customer_note_pinned BOOLEAN NOT NULL DEFAULT FALSE,
    customer_note_customer_id BIGINT NOT NULL, -- Which customer the note belongs to
    customer_note_created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    customer_note_created_by BIGINT DEFAULT NULL, -- Who created the note
    customer_note_updated_at DATETIME DEFAULT NULL,
    customer_note_updated_by BIGINT DEFAULT NULL,
    customer_note_deleted_at DATETIME DEFAULT NULL,
    customer_note_deleted_by BIGINT DEFAULT NULL
);

-- ================================================
-- Add Foreign Keys after creating tables
-- ================================================

-- 1. Link customers to tenants and users
ALTER TABLE customers
ADD CONSTRAINT fk_customers_tenant
FOREIGN KEY (customer_tenant_id)
REFERENCES tenants(tenant_id)
ON DELETE CASCADE
ON UPDATE CASCADE,
ADD CONSTRAINT fk_customers_created_by
FOREIGN KEY (customer_created_by)
REFERENCES users(user_id)
ON DELETE SET NULL
ON UPDATE CASCADE,
ADD CONSTRAINT fk_customers_updated_by
FOREIGN KEY (customer_updated_by)
REFERENCES users(user_id)
ON DELETE SET NULL
ON UPDATE CASCADE,
ADD CONSTRAINT fk_customers_deleted_by
FOREIGN KEY (customer_deleted_by)
REFERENCES users(user_id)
ON DELETE SET NULL
ON UPDATE CASCADE,
ADD CONSTRAINT fk_customers_user_id_agent
FOREIGN KEY (customer_user_id_agent)
REFERENCES users(user_id)
ON DELETE SET NULL
ON UPDATE CASCADE;

-- 2. Link customer_billings to customers and users
ALTER TABLE customer_billings
ADD CONSTRAINT fk_customer_billings_customer
FOREIGN KEY (customer_billing_customer_id)
REFERENCES customers(customer_id)
ON DELETE CASCADE
ON UPDATE CASCADE,
ADD CONSTRAINT fk_customer_billings_created_by
FOREIGN KEY (customer_billing_created_by)
REFERENCES users(user_id)
ON DELETE SET NULL
ON UPDATE CASCADE,
ADD CONSTRAINT fk_customer_billings_updated_by
FOREIGN KEY (customer_billing_updated_by)
REFERENCES users(user_id)
ON DELETE SET NULL
ON UPDATE CASCADE,
ADD CONSTRAINT fk_customer_billings_deleted_by
FOREIGN KEY (customer_billing_deleted_by)
REFERENCES users(user_id)
ON DELETE SET NULL
ON UPDATE CASCADE;

-- 3. Link customer_notes to customers and users
ALTER TABLE customer_notes
ADD CONSTRAINT fk_customer_notes_customer
FOREIGN KEY (customer_note_customer_id)
REFERENCES customers(customer_id)
ON DELETE CASCADE
ON UPDATE CASCADE,
ADD CONSTRAINT fk_customer_notes_created_by
FOREIGN KEY (customer_note_created_by)
REFERENCES users(user_id)
ON DELETE SET NULL
ON UPDATE CASCADE,
ADD CONSTRAINT fk_customer_notes_updated_by
FOREIGN KEY (customer_note_updated_by)
REFERENCES users(user_id)
ON DELETE SET NULL
ON UPDATE CASCADE,
ADD CONSTRAINT fk_customer_notes_deleted_by
FOREIGN KEY (customer_note_deleted_by)
REFERENCES users(user_id)
ON DELETE SET NULL
ON UPDATE CASCADE;

-- ================================================
-- Add Indexes (Highly Recommended for performance)
-- ================================================

CREATE INDEX idx_customer_tenant_id ON customers(customer_tenant_id);
CREATE INDEX idx_customer_user_id_agent ON customers(customer_user_id_agent);
CREATE INDEX idx_customer_billing_customer_id ON customer_billings(customer_billing_customer_id);
CREATE INDEX idx_customer_note_customer_id ON customer_notes(customer_note_customer_id);
CREATE INDEX idx_customer_note_created_by ON customer_notes(customer_note_created_by);
