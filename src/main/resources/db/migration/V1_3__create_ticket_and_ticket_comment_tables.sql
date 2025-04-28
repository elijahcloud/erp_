-- ============================
-- v1__create_ticket.sql
-- Database Schema Initialization
-- ============================

-- Department Table
CREATE TABLE departments (
    department_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(255) NOT NULL,
    department_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    department_created_by BIGINT DEFAULT NULL,
    department_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    department_updated_by BIGINT DEFAULT NULL,
    department_deleted_at DATETIME DEFAULT NULL,
    department_deleted_by BIGINT DEFAULT NULL
);

-- Tickets Table
CREATE TABLE tickets (
    ticket_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_customer_id BIGINT NOT NULL,
    ticket_title VARCHAR(255) NOT NULL,
    ticket_type ENUM('COMPLAINT', 'SERVICE_REQUEST', 'INFO_REQUEST', 'TECHNICAL_SUPPORT') NULL,
    ticket_description TEXT NULL,
    ticket_priority ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL') NULL,
    ticket_status ENUM('OPEN', 'ASSIGNED', 'IN_PROGRESS', 'ESCALATED', 'RESOLVED', 'CLOSED') NULL,
    ticket_source VARCHAR(255) NULL,
    ticket_assigned_department_id BIGINT NULL,
    ticket_assigned_agent VARCHAR(255) NULL,
    ticket_created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ticket_created_by BIGINT DEFAULT NULL,
    ticket_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    ticket_updated_by BIGINT DEFAULT NULL,
    ticket_deleted BOOLEAN DEFAULT FALSE NOT NULL,
    ticket_deleted_at DATETIME DEFAULT NULL,
    ticket_deleted_by BIGINT DEFAULT NULL
);

-- Ticket Comments Table
CREATE TABLE ticket_comments (
    ticket_comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_comment_ticket_id BIGINT NOT NULL,
    ticket_comment_text TEXT NOT NULL,
    ticket_comment_author VARCHAR(255) NULL,
    ticket_comment_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ticket_comment_created_by BIGINT DEFAULT NULL,
    ticket_comment_updated_at DATETIME DEFAULT NULL,
    ticket_comment_updated_by BIGINT DEFAULT NULL,
    ticket_comment_deleted_at DATETIME DEFAULT NULL,
    ticket_comment_deleted_by BIGINT DEFAULT NULL
);

-- Add Foreign Keys for departments
ALTER TABLE departments
ADD CONSTRAINT fk_departments_created_by
FOREIGN KEY (department_created_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
ADD CONSTRAINT fk_departments_updated_by
FOREIGN KEY (department_updated_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
ADD CONSTRAINT fk_departments_deleted_by
FOREIGN KEY (department_deleted_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE;

-- Add Foreign Keys for tickets
ALTER TABLE tickets
ADD CONSTRAINT fk_tickets_customer
FOREIGN KEY (ticket_customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT fk_tickets_assigned_department
FOREIGN KEY (ticket_assigned_department_id) REFERENCES departments(department_id) ON DELETE SET NULL ON UPDATE CASCADE,
ADD CONSTRAINT fk_tickets_created_by
FOREIGN KEY (ticket_created_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
ADD CONSTRAINT fk_tickets_updated_by
FOREIGN KEY (ticket_updated_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
ADD CONSTRAINT fk_tickets_deleted_by
FOREIGN KEY (ticket_deleted_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE;

-- Add Foreign Keys for ticket_comments
ALTER TABLE ticket_comments
ADD CONSTRAINT fk_ticket_comments_ticket
FOREIGN KEY (ticket_comment_ticket_id) REFERENCES tickets(ticket_id) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT fk_ticket_comments_created_by
FOREIGN KEY (ticket_comment_created_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
ADD CONSTRAINT fk_ticket_comments_updated_by
FOREIGN KEY (ticket_comment_updated_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
ADD CONSTRAINT fk_ticket_comments_deleted_by
FOREIGN KEY (ticket_comment_deleted_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE;


