CREATE TABLE IF NOT EXISTS core_schema.outbox_events (
    id UUID PRIMARY KEY,
    aggregate_id UUID NOT NULL,
    event_type VARCHAR(80) NOT NULL,
    destination_type VARCHAR(40) NOT NULL,
    status VARCHAR(20) NOT NULL,
    payload TEXT NOT NULL,
    retry_count INTEGER NOT NULL DEFAULT 0,
    last_error VARCHAR(500),
    next_retry_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    sent_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX IF NOT EXISTS idx_outbox_events_status_next_retry_at
    ON core_schema.outbox_events(status, next_retry_at);

CREATE INDEX IF NOT EXISTS idx_outbox_events_aggregate_id
    ON core_schema.outbox_events(aggregate_id);
