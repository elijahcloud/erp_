CREATE INDEX idx_customer_id ON tickets(ticket_customer_id);

CREATE INDEX idx_ticket_status ON tickets(ticket_status);

CREATE INDEX idx_ticket_id ON tickets(ticket_id);

CREATE INDEX idx_ticket_priority ON tickets(ticket_priority);

CREATE INDEX idx_first_response_at ON tickets(first_response_at);

CREATE INDEX idx_sla_resolution_due_at ON tickets(sla_resolution_due_at);

CREATE INDEX idx_ticket_deleted ON tickets(ticket_deleted);

CREATE INDEX idx_ticket_type ON tickets(ticket_type);

CREATE INDEX idx_assigned_department_name ON tickets(ticket_assigned_department_id);

CREATE INDEX idx_customer_status ON tickets(ticket_customer_id, ticket_status);

CREATE INDEX idx_resolution_sla_status ON tickets(is_resolution_sla_breached, ticket_status);

CREATE INDEX idx_ticket_assigned_agent ON tickets(ticket_assigned_agent);

