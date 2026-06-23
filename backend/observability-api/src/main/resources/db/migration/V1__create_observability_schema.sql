CREATE SCHEMA IF NOT EXISTS observability_schema;

CREATE TABLE IF NOT EXISTS observability_schema.audit_events (
    id UUID PRIMARY KEY,
    event_id UUID NOT NULL UNIQUE,
    source_account_id UUID,
    target_account_id UUID,
    event_kind VARCHAR(32) NOT NULL,
    event_status VARCHAR(32) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    event_key VARCHAR(128) NOT NULL,
    occurred_at TIMESTAMP WITH TIME ZONE NOT NULL,
    received_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_audit_events_event_id
    ON observability_schema.audit_events(event_id);

CREATE INDEX IF NOT EXISTS idx_audit_events_occurred_at
    ON observability_schema.audit_events(occurred_at);
