-- Create ENUM types
-- MariaDB allows ENUM types to be used directly in columns, so we won't need to explicitly define them globally.
-- They will be declared directly in the table definition.

-- Department Table
CREATE TABLE departments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Customer Table
CREATE TABLE customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    contact_tier ENUM('BASIC', 'GOLD', 'SILVER') NULL,
    email VARCHAR(255) NULL,
    department_id BIGINT,
    account_type ENUM('RETAIL', 'SME', 'ENTERPRISE') NULL,
    phone_number VARCHAR(255) NULL,
    company_name VARCHAR(255) NULL,
    FOREIGN KEY (department_id) REFERENCES departments(id)
);

-- Tickets Table
CREATE TABLE tickets (
    ticket_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_title VARCHAR(255) NOT NULL,
    ticket_type ENUM('COMPLAINT', 'SERVICE_REQUEST', 'INFO_REQUEST', 'TECHNICAL_SUPPORT') NULL,
    ticket_description TEXT NULL,
    ticket_priority ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL') NULL,
    ticket_status ENUM('OPEN', 'ASSIGNED', 'IN_PROGRESS', 'ESCALATED', 'RESOLVED', 'CLOSED') NULL,
    ticket_source VARCHAR(255) NULL,
    ticket_customer_id BIGINT NOT NULL,
    ticket_assigned_department_id BIGINT NULL,
    ticket_assigned_agent VARCHAR(255) NULL,
    ticket_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ticket_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    ticket_deleted BOOLEAN DEFAULT FALSE NOT NULL,
    FOREIGN KEY (ticket_customer_id) REFERENCES customers(id),
    FOREIGN KEY (ticket_assigned_department_id) REFERENCES departments(id)
);

-- Ticket Comments Table
CREATE TABLE ticket_comments (
    ticket_comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_comment_text TEXT NOT NULL,
    ticket_comment_author VARCHAR(255) NULL,
    ticket_comment_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ticket_comment_ticket_id BIGINT NOT NULL,
    FOREIGN KEY (ticket_comment_ticket_id) REFERENCES tickets(ticket_id)
);


