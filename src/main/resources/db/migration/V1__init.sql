-- ============================
-- v1__init.sql
-- Database Schema Initialization
-- ============================

-- Create base tables first
CREATE TABLE users (
  user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_email VARCHAR(255) UNIQUE NOT NULL,
  user_password TEXT NOT NULL,
  user_is_active TINYINT(1) DEFAULT 1,
  user_active_user_role_id BIGINT DEFAULT NULL,
  user_created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  user_created_by BIGINT DEFAULT NULL,
  user_updated_at DATETIME DEFAULT NULL,
  user_updated_by BIGINT DEFAULT NULL,
  user_deleted_at DATETIME DEFAULT NULL,
  user_deleted_by BIGINT DEFAULT NULL
) ENGINE=InnoDB;

CREATE TABLE tenants (
  tenant_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  tenant_name VARCHAR(255) NOT NULL,
  tenant_code VARCHAR(100) UNIQUE NOT NULL,
  tenant_is_landlord TINYINT(1) DEFAULT 0,
  tenant_is_active TINYINT(1) DEFAULT 1,
  tenant_logo VARCHAR(255) DEFAULT NULL,
  tenant_contact_email VARCHAR(70) DEFAULT NULL,
  tenant_contact_phone VARCHAR(20) DEFAULT NULL,
  tenant_address VARCHAR(255) DEFAULT NULL,
  tenant_created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  tenant_created_by BIGINT DEFAULT NULL,
  tenant_updated_at DATETIME DEFAULT NULL,
  tenant_updated_by BIGINT DEFAULT NULL,
  tenant_deleted_at DATETIME DEFAULT NULL,
  tenant_deleted_by BIGINT DEFAULT NULL
) ENGINE=InnoDB;

-- Create dependent tables
CREATE TABLE roles (
  role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  role_name VARCHAR(100) UNIQUE NOT NULL,
  role_description TEXT DEFAULT NULL,
  role_scope VARCHAR(20) CHECK (role_scope IN ('LANDLORD', 'TENANT', 'GLOBAL')),
  role_created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  role_updated_at DATETIME DEFAULT NULL,
  role_created_by BIGINT DEFAULT NULL,
  role_updated_by BIGINT DEFAULT NULL,
  role_deleted_at DATETIME DEFAULT NULL,
  role_deleted_by BIGINT DEFAULT NULL
) ENGINE=InnoDB;


CREATE TABLE user_roles (
  user_role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_role_user_id BIGINT NOT NULL,
  user_role_tenant_id BIGINT NOT NULL,
  user_role_role_id BIGINT NOT NULL,
  user_role_created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  user_role_created_by BIGINT DEFAULT NULL,
  user_role_updated_at DATETIME DEFAULT NULL,
  user_role_updated_by BIGINT DEFAULT NULL,
  user_role_deleted_at DATETIME DEFAULT NULL,
  user_role_deleted_by BIGINT DEFAULT NULL,
  CONSTRAINT fk_user_roles_user FOREIGN KEY (user_role_user_id) REFERENCES users(user_id),
  CONSTRAINT fk_user_roles_tenant FOREIGN KEY (user_role_tenant_id) REFERENCES tenants(tenant_id),
  CONSTRAINT fk_user_roles_role FOREIGN KEY (user_role_role_id) REFERENCES roles(role_id),
  CONSTRAINT uc_user_role_tenant UNIQUE (user_role_user_id, user_role_tenant_id, user_role_role_id)
) ENGINE=InnoDB;

CREATE TABLE tenant_domains (
  tenant_domain_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  tenant_domain_tenant_id BIGINT NOT NULL,
  tenant_domain_name VARCHAR(255) NOT NULL UNIQUE,
  tenant_domain_is_primary TINYINT(1) DEFAULT 1,
  tenant_domain_created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  tenant_domain_created_by BIGINT DEFAULT NULL,
  tenant_domain_updated_at DATETIME DEFAULT NULL,
  tenant_domain_updated_by BIGINT DEFAULT NULL,
  tenant_domain_deleted_at DATETIME DEFAULT NULL,
  tenant_domain_deleted_by BIGINT DEFAULT NULL
) ENGINE=InnoDB;

CREATE TABLE user_tenants (
  user_tenant_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_tenant_user_id BIGINT NOT NULL,
  user_tenant_tenant_id BIGINT NOT NULL,
  user_tenant_is_primary TINYINT(1) DEFAULT 0,
  user_tenant_created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  user_tenant_created_by BIGINT DEFAULT NULL,
  user_tenant_updated_at DATETIME DEFAULT NULL,
  user_tenant_updated_by BIGINT DEFAULT NULL,
  user_tenant_deleted_at DATETIME DEFAULT NULL,
  user_tenant_deleted_by BIGINT DEFAULT NULL,
  CONSTRAINT fk_user_tenants_user FOREIGN KEY (user_tenant_user_id) REFERENCES users(user_id),
  CONSTRAINT fk_user_tenants_tenant FOREIGN KEY (user_tenant_tenant_id) REFERENCES tenants(tenant_id),
  CONSTRAINT uc_user_tenant UNIQUE (user_tenant_user_id, user_tenant_tenant_id)
) ENGINE=InnoDB;

CREATE TABLE permissions (
  permission_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  permission_code VARCHAR(100) UNIQUE NOT NULL, -- e.g., 'view_financial_reports'
  permission_name VARCHAR(255) NOT NULL,        -- Human-friendly name
  permission_module VARCHAR(100) NOT NULL,      -- e.g., 'finance', 'hr'
  permission_description TEXT DEFAULT NULL,
  permission_created_at DATETIME DEFAULT NULL,
  permission_created_by BIGINT DEFAULT NULL,
  permission_updated_at DATETIME DEFAULT NULL,
  permission_updated_by BIGINT DEFAULT NULL,
  permission_deleted_at DATETIME DEFAULT NULL,
  permission_deleted_by BIGINT DEFAULT NULL
) ENGINE=InnoDB;

CREATE TABLE role_permissions (
  role_permission_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  role_permission_role_id BIGINT NOT NULL,
  role_permission_permission_id BIGINT NOT NULL,
  CONSTRAINT fk_role_permissions_role FOREIGN KEY (role_permission_role_id) REFERENCES roles(role_id),
  CONSTRAINT fk_role_permissions_permission FOREIGN KEY (role_permission_permission_id) REFERENCES permissions(permission_id),
  CONSTRAINT uc_role_permission UNIQUE (role_permission_role_id, role_permission_permission_id)
) ENGINE=InnoDB;

CREATE TABLE audit_logs (
  audit_log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  audit_log_table_name VARCHAR(255) DEFAULT NULL,
  audit_log_action VARCHAR(10) CHECK (audit_log_action IN ('INSERT', 'UPDATE', 'DELETE')),
  audit_log_record_id BIGINT DEFAULT NULL,
  audit_log_data_before JSON DEFAULT NULL,
  audit_log_data_after JSON DEFAULT NULL,
  audit_log_performed_by BIGINT DEFAULT NULL,
  audit_log_performed_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Add explicit indexes for referenced columns
ALTER TABLE tenants ADD INDEX idx_tenant_id (tenant_id);
ALTER TABLE users ADD INDEX idx_user_id (user_id);

-- Add foreign key constraints after all tables are created
ALTER TABLE users
    ADD CONSTRAINT fk_user_created_by FOREIGN KEY (user_created_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    ADD CONSTRAINT fk_user_updated_by FOREIGN KEY (user_updated_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    ADD CONSTRAINT fk_user_deleted_by FOREIGN KEY (user_deleted_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    ADD CONSTRAINT fk_user_active_user_role_id FOREIGN KEY (user_active_user_role_id) REFERENCES roles(role_id) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tenants
    ADD CONSTRAINT fk_tenant_created_by FOREIGN KEY (tenant_created_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    ADD CONSTRAINT fk_tenant_updated_by FOREIGN KEY (tenant_updated_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    ADD CONSTRAINT fk_tenant_deleted_by FOREIGN KEY (tenant_deleted_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE roles
    ADD CONSTRAINT fk_role_created_by FOREIGN KEY (role_created_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    ADD CONSTRAINT fk_role_updated_by FOREIGN KEY (role_updated_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    ADD CONSTRAINT fk_role_deleted_by FOREIGN KEY (role_deleted_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_role_created_by FOREIGN KEY (user_role_created_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    ADD CONSTRAINT fk_user_role_updated_by FOREIGN KEY (user_role_updated_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    ADD CONSTRAINT fk_user_role_deleted_by FOREIGN KEY (user_role_deleted_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE user_tenants
    ADD CONSTRAINT fk_user_tenant_created_by FOREIGN KEY (user_tenant_created_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    ADD CONSTRAINT fk_user_tenant_updated_by FOREIGN KEY (user_tenant_updated_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    ADD CONSTRAINT fk_user_tenant_deleted_by FOREIGN KEY (user_tenant_deleted_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE tenant_domains
    ADD CONSTRAINT fk_domain_created_by FOREIGN KEY (tenant_domain_created_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    ADD CONSTRAINT fk_domain_updated_by FOREIGN KEY (tenant_domain_updated_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    ADD CONSTRAINT fk_domain_deleted_by FOREIGN KEY (tenant_domain_deleted_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE permissions
    ADD CONSTRAINT fk_permission_created_by FOREIGN KEY (permission_created_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    ADD CONSTRAINT fk_permission_updated_by FOREIGN KEY (permission_updated_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE,
    ADD CONSTRAINT fk_permission_deleted_by FOREIGN KEY (permission_deleted_by) REFERENCES users(user_id) ON DELETE SET NULL ON UPDATE CASCADE;

-- Seed initial roles
INSERT INTO roles (role_name, role_description, role_scope, role_created_at, role_created_by, role_updated_by)
VALUES
    ('Platform Administrator', 'Full access to all platform features and settings across all tenants', 'GLOBAL', NOW(), NULL, NULL),
    ('Platform Support Admininstrator', 'Access to support features and limited settings across all tenants', 'GLOBAL', NOW(), NULL, NULL),
    ('Tenant Admin', 'Manages a tenant organization’s users and settings', 'TENANT', NOW(), NULL, NULL),
    ('HR Administrator', 'Full access to HR module and related features', 'TENANT', NOW(), NULL, NULL),
    ('Finance Manager', 'Access to financial data, reports, and approval workflows', 'TENANT', NOW(), NULL, NULL),
    ('Crm Manager', 'Oversees CRM activities and customer lifecycle', 'TENANT', NOW(), NULL, NULL),
    ('Compliance Manager', 'Manages SLAs and compliance checks', 'TENANT', NOW(), NULL, NULL),
    ('Sales Manager', 'Access to CRM and sales-related features', 'TENANT', NOW(), NULL, NULL),
    ('Operations Manager', 'Manages day-to-day operations and reports', 'TENANT', NOW(), NULL, NULL),
    ('Executive User', 'Access to executive dashboards and reports', 'TENANT', NOW(), NULL, NULL),
    ('Support Agent', 'Handles tickets and technical support tasks', 'TENANT', NOW(), NULL, NULL),
    ('Project Officer', 'Coordinates installations and field projects', 'TENANT', NOW(), NULL, NULL),
    ('Field Technician', 'Executes field activities and work orders', 'TENANT', NOW(), NULL, NULL),
    ('Legal Officer', 'Handles legal processes and documentation', 'TENANT', NOW(), NULL, NULL),
    ('Auditor', 'Audits system activity and tenant behavior', 'TENANT', NOW(), NULL, NULL);

-- Seed permissions
INSERT INTO permissions (permission_code, permission_name, permission_module, permission_description, permission_created_at)
VALUES
    -- Finance module
    ('view_financial_reports', 'View Financial Reports', 'Finance', 'Allows viewing of financial reports', NOW()),
    ('create_financial_entries', 'Create Financial Entries', 'Finance', 'Allows creation of financial entries', NOW()),
    ('edit_financial_data', 'Edit Financial Data', 'Finance', 'Allows editing of financial data', NOW()),
    ('delete_financial_records', 'Delete Financial Records', 'Finance', 'Allows deletion of financial records', NOW()),
    ('approve_financial_transactions', 'Approve Financial Transactions', 'Finance', 'Allows approval of financial transactions', NOW()),

    -- HR module
    ('view_employee_records', 'View Employee Records', 'HR', 'Allows viewing of employee records', NOW()),
    ('create_employee_profiles', 'Create Employee Profiles', 'HR', 'Allows creation of employee profiles', NOW()),
    ('edit_employee_information', 'Edit Employee Information', 'HR', 'Allows editing of employee information', NOW()),
    ('delete_employee_profile', 'Delete Employee Profile', 'HR', 'Allows deletion of employee profiles', NOW()),
    ('approve_leave_requests', 'Approve Leave Requests', 'HR', 'Allows approval of leave requests', NOW()),

    -- Inventory module
    ('view_inventory', 'View Inventory', 'Inventory', 'Allows viewing of inventory data', NOW()),
    ('create_inventory_item', 'Create Inventory Item', 'Inventory', 'Allows creation of inventory items', NOW()),
    ('edit_inventory_data', 'Edit Inventory Data', 'Inventory', 'Allows editing of inventory data', NOW()),
    ('delete_inventory_items', 'Delete Inventory Items', 'Inventory', 'Allows deletion of inventory items', NOW()),
    ('approve_inventory_adjustments', 'Approve Inventory Adjustments', 'Inventory', 'Allows approval of inventory adjustments', NOW()),

    -- Sales module
    ('view_sales_data', 'View Sales Data', 'Sales', 'Allows viewing of sales data', NOW()),
    ('create_sales_order', 'Create Sales Order', 'Sales', 'Allows creation of sales orders', NOW()),
    ('edit_sales_information', 'Edit Sales Information', 'Sales', 'Allows editing of sales information', NOW()),
    ('delete_sales_records', 'Delete Sales Records', 'Sales', 'Allows deletion of sales records', NOW()),
    ('approve_sales_discount', 'Approve Sales Discount', 'Sales', 'Allows approval of sales discounts', NOW()),

    -- System module
    ('system_configuration', 'System Configuration', 'System', 'Allows configuration of system settings', NOW()),
    ('user_management', 'User Management', 'System', 'Allows management of users', NOW()),
    ('role_management', 'Role Management', 'System', 'Allows management of roles', NOW()),
    ('audit_log_access', 'Audit Log Access', 'System', 'Allows access to audit logs', NOW());

-- Seed landlord tenant and domain
INSERT INTO tenants (tenant_name, tenant_code, tenant_is_landlord, tenant_created_at)
VALUES ('VDT', 'VDT001', 1, NOW())
ON DUPLICATE KEY UPDATE tenant_name = tenant_name;

INSERT INTO tenant_domains (tenant_domain_tenant_id, tenant_domain_name, tenant_domain_is_primary, tenant_domain_created_at)
SELECT tenant_id, 'vdt.com', 1, NOW()
FROM tenants
WHERE tenant_code = 'VDT001'
ON DUPLICATE KEY UPDATE tenant_domain_name = tenant_domain_name;