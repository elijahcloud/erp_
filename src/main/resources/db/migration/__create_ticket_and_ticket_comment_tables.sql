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

-- Ticket Table
CREATE TABLE tickets (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         ticket_title VARCHAR(255) NOT NULL,
                         type ENUM('COMPLAINT', 'SERVICE_REQUEST', 'INFO_REQUEST', 'TECHNICAL_SUPPORT') NULL,
                         description TEXT NULL,
                         priority ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL') NULL,
                         status ENUM('OPEN', 'ASSIGNED', 'IN_PROGRESS', 'ESCALATED', 'RESOLVED', 'CLOSED') NULL,
                         source VARCHAR(255) NULL,
                         customer_id BIGINT NOT NULL,
                         assigned_department_id BIGINT NULL,
                         assigned_agent VARCHAR(255) NULL,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                         deleted BOOLEAN DEFAULT FALSE NOT NULL,
                         FOREIGN KEY (customer_id) REFERENCES customers(id),
                         FOREIGN KEY (assigned_department_id) REFERENCES departments(id)
);

-- Ticket Comment Table
CREATE TABLE ticket_comments (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 comment TEXT NOT NULL,
                                 author VARCHAR(255) NULL,
                                 timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                 ticket_id BIGINT NOT NULL,
                                 FOREIGN KEY (ticket_id) REFERENCES tickets(id)
);


