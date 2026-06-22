CREATE SCHEMA IF NOT EXISTS core_schema;

CREATE TABLE core_schema.operations (
    id UUID PRIMARY KEY,
    source_account_id UUID,
    target_account_id UUID,
    kind VARCHAR(40) NOT NULL,
    status VARCHAR(40) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    idempotency_key VARCHAR(120) NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);
