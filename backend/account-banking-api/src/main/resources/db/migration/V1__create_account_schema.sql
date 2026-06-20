CREATE SCHEMA IF NOT EXISTS account_schema;

CREATE TABLE account_schema.accounts (
    id UUID PRIMARY KEY,
    owner_id UUID NOT NULL,
    iban VARCHAR(34) NOT NULL UNIQUE,
    status VARCHAR(40) NOT NULL,
    balance_amount NUMERIC(19, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
